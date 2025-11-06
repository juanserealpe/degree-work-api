package co.edu.unicauca.services.auth;

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
    private RefreshTokenRepository _refreshTokenRepository;

    @Transactional
    public RefreshToken createRefreshToken(Account account) {
        Logger.info(getClass(), "Creating refresh token for account ID: " + account.getIdAccount());

        revokeTokensByAccount(account);
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setAccount(account);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setRevoked(false);

        RefreshToken saved = _refreshTokenRepository.save(refreshToken);

        Logger.success(getClass(), "Refresh token created successfully for account ID: "
                + account.getIdAccount());

        return saved;
    }

    public RefreshToken findByToken(String token) {
        return _refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenRefreshException(token, "Refresh token not found"));
    }

    @Transactional
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.isRevoked()) {
            Logger.warn(getClass(), "Refresh token has been revoked: " + token.getToken());
            throw new TokenRefreshException(token.getToken(), "Refresh token has been revoked");
        }

        if (token.isExpired()) {
            Logger.warn(getClass(), "Refresh token has expired: " + token.getToken());
            _refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(),
                    "Refresh token has expired. Please login again");
        }

        return token;
    }

    @Transactional
    public void revokeToken(String token) {
        Logger.info(getClass(), "Revoking refresh token");

        Optional<RefreshToken> refreshToken = _refreshTokenRepository.findByToken(token);
        if (refreshToken.isPresent()) {
            RefreshToken rt = refreshToken.get();
            rt.setRevoked(true);
            _refreshTokenRepository.save(rt);
            Logger.success(getClass(), "Refresh token revoked successfully");
        }
    }

    @Transactional
    public void revokeTokensByAccount(Account account) {
        Logger.info(getClass(), "Revoking all tokens for account ID: " + account.getIdAccount());
        _refreshTokenRepository.revokeAllByAccountId(account.getIdAccount());
    }

    @Transactional
    public void deleteTokensByAccount(Account account) {
        Logger.info(getClass(), "Deleting all tokens for account ID: " + account.getIdAccount());
        _refreshTokenRepository.deleteByAccountId(account.getIdAccount());
    }

    /**
     * Limpieza de tokens expirados (ejecutar periódicamente)
     */
    @Transactional
    public int cleanupExpiredTokens() {
        Logger.info(getClass(), "Cleaning up expired refresh tokens");
        int deletedCount = _refreshTokenRepository.deleteExpiredTokens(Instant.now());
        Logger.success(getClass(), "Deleted " + deletedCount + " expired refresh tokens");
        return deletedCount;
    }
}