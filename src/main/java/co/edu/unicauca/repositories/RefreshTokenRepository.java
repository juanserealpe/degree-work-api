package co.edu.unicauca.repositories;

import co.edu.unicauca.entities.Account;
import co.edu.unicauca.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    /**
     * Busca un refresh token por su valor
     */
    Optional<RefreshToken> findByToken(String token);

    /**
     * Busca un refresh token activo por cuenta
     */
    Optional<RefreshToken> findByAccountAndRevokedFalse(Account account);

    /**
     * Elimina todos los tokens de una cuenta
     */
    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.account.idAccount = :accountId")
    void deleteByAccountId(Long accountId);

    /**
     * Elimina tokens expirados (limpieza periódica)
     */
    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.expiryDate < :now")
    int deleteExpiredTokens(Instant now);

    /**
     * Revoca todos los tokens de una cuenta
     */
    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.revoked = true WHERE rt.account.idAccount = :accountId")
    void revokeAllByAccountId(Long accountId);
}