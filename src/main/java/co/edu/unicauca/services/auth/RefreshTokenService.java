package co.edu.unicauca.services.auth;

import co.edu.unicauca.entities.Account;
import co.edu.unicauca.entities.RefreshToken;
import co.edu.unicauca.enums.exceptions.RefreshTokenErrorCode;
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

    @Value("${jwt.refreshExpirationMs:604800000}") // 7 days
    private Long refreshTokenDurationMs;

    @Autowired
    private RefreshTokenRepository _refreshTokenRepository;

    @Transactional
    public RefreshToken createRefreshToken(Account account) {
        Logger.info(getClass(), "creating refresh token for account ID: " + account.getIdAccount());

        try {
            revokeTokensByAccount(account);

            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setAccount(account);
            refreshToken.setToken(UUID.randomUUID().toString());
            refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
            refreshToken.setRevoked(false);

            RefreshToken saved = _refreshTokenRepository.save(refreshToken);

            Logger.success(getClass(), "refresh token created successfully for account ID: "
                    + account.getIdAccount());

            return saved;
        } catch (Exception e) {
            Logger.error(getClass(), "error generating refresh token: " + e.getMessage());
            throw new TokenRefreshException(null, RefreshTokenErrorCode.TOKEN_CREATION_FAILED);
        }
    }

    public RefreshToken findByToken(String token) {
        return _refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenRefreshException(token, RefreshTokenErrorCode.TOKEN_NOT_FOUND));
    }

    @Transactional
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.isRevoked()) {
            Logger.warn(getClass(), "refresh token has been revoked: " + token.getToken());
            throw new TokenRefreshException(token.getToken(), RefreshTokenErrorCode.TOKEN_REVOKED);
        }

        if (token.isExpired()) {
            Logger.warn(getClass(), "refresh token has expired: " + token.getToken());
            _refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), RefreshTokenErrorCode.TOKEN_EXPIRED);
        }

        return token;
    }

    @Transactional
    public void revokeToken(String token) {
        Logger.info(getClass(), "revoking refresh token");
        try {
            Optional<RefreshToken> refreshToken = _refreshTokenRepository.findByToken(token);
            if (refreshToken.isPresent()) {
                RefreshToken rt = refreshToken.get();
                rt.setRevoked(true);
                _refreshTokenRepository.save(rt);
                Logger.success(getClass(), "refresh token revoked successfully");
            } else {
                throw new TokenRefreshException(token, RefreshTokenErrorCode.TOKEN_NOT_FOUND);
            }
        } catch (Exception e) {
            Logger.error(getClass(), "error revoking refresh token: " + e.getMessage());
            throw new TokenRefreshException(token, RefreshTokenErrorCode.TOKEN_REVOCATION_FAILED);
        }
    }

    @Transactional
    public void revokeTokensByAccount(Account account) {
        Logger.info(getClass(), "revoking all tokens for account ID: " + account.getIdAccount());
        _refreshTokenRepository.revokeAllByAccountId(account.getIdAccount());
    }

    @Transactional
    public void deleteTokensByAccount(Account account) {
        Logger.info(getClass(), "deleting all tokens for account ID: " + account.getIdAccount());
        _refreshTokenRepository.deleteByAccountId(account.getIdAccount());
    }

    @Transactional
    public int cleanupExpiredTokens() {
        Logger.info(getClass(), "cleaning up expired refresh tokens");
        try {
            int deletedCount = _refreshTokenRepository.deleteExpiredTokens(Instant.now());
            Logger.success(getClass(), "deleted " + deletedCount + " expired refresh tokens");
            return deletedCount;
        } catch (Exception e) {
            Logger.error(getClass(), "error cleaning up expired tokens: " + e.getMessage());
            throw new TokenRefreshException(null, RefreshTokenErrorCode.TOKEN_CLEANUP_FAILED);
        }
    }
}
