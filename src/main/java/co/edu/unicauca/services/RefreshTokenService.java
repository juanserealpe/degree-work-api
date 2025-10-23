package co.edu.unicauca.services;

import co.edu.unicauca.entities.Account;
import co.edu.unicauca.entities.RefreshToken;
import co.edu.unicauca.exceptions.TokenRefreshException;
import co.edu.unicauca.repositories.RefreshTokenRepository;
import co.edu.unicauca.utilities.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${jwt.refreshExpirationMs:604800000}") // 7 días por defecto
    private Long refreshTokenDurationMs;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    /**
     * Crea un refresh token para una cuenta
     * Si ya existe uno, lo revoca y crea uno nuevo
     */
    @Transactional
    public RefreshToken createRefreshToken(Account account) {
        Logger.info(getClass(), "Creating refresh token for account ID: " + account.getIdAccount());

        // Revocar tokens existentes de esta cuenta
        revokeTokensByAccount(account);

        // Crear nuevo refresh token
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setAccount(account);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setRevoked(false);

        RefreshToken saved = refreshTokenRepository.save(refreshToken);

        Logger.success(getClass(), "Refresh token created successfully for account ID: "
                + account.getIdAccount());

        return saved;
    }

    /**
     * Busca y valida un refresh token
     */
    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenRefreshException(token, "Refresh token not found"));
    }

    /**
     * Verifica si un refresh token es válido
     * Un token es válido si existe, no está expirado y no está revocado
     */
    @Transactional
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.isRevoked()) {
            Logger.warn(getClass(), "Refresh token has been revoked: " + token.getToken());
            throw new TokenRefreshException(token.getToken(), "Refresh token has been revoked");
        }

        if (token.isExpired()) {
            Logger.warn(getClass(), "Refresh token has expired: " + token.getToken());
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(),
                    "Refresh token has expired. Please login again");
        }

        return token;
    }

    /**
     * Revoca un refresh token específico
     */
    @Transactional
    public void revokeToken(String token) {
        Logger.info(getClass(), "Revoking refresh token");

        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(token);
        if (refreshToken.isPresent()) {
            RefreshToken rt = refreshToken.get();
            rt.setRevoked(true);
            refreshTokenRepository.save(rt);
            Logger.success(getClass(), "Refresh token revoked successfully");
        }
    }

    /**
     * Revoca todos los tokens de una cuenta (útil para logout en todos los dispositivos)
     */
    @Transactional
    public void revokeTokensByAccount(Account account) {
        Logger.info(getClass(), "Revoking all tokens for account ID: " + account.getIdAccount());
        refreshTokenRepository.revokeAllByAccountId(account.getIdAccount());
    }

    /**
     * Elimina físicamente todos los tokens de una cuenta
     */
    @Transactional
    public void deleteTokensByAccount(Account account) {
        Logger.info(getClass(), "Deleting all tokens for account ID: " + account.getIdAccount());
        refreshTokenRepository.deleteByAccountId(account.getIdAccount());
    }

    /**
     * Limpieza de tokens expirados (ejecutar periódicamente)
     */
    @Transactional
    public int cleanupExpiredTokens() {
        Logger.info(getClass(), "Cleaning up expired refresh tokens");
        int deletedCount = refreshTokenRepository.deleteExpiredTokens(Instant.now());
        Logger.success(getClass(), "Deleted " + deletedCount + " expired refresh tokens");
        return deletedCount;
    }
}