package com.moontech.archetype.domain.repository;

import com.moontech.archetype.domain.entity.PasswordResetTokenEntity;
import java.time.Instant;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository for password reset token persistence operations.
 *
 * <p>Provides lookup by token hash and cleanup of expired tokens.
 *
 * @author Felipe Monzón
 * @since 2026-07-21
 */
public interface PasswordResetTokenRepository
    extends JpaRepository<PasswordResetTokenEntity, Long> {
  /**
   * Finds a password reset token entity by its hashed token value.
   *
   * @param tokenHash SHA-256 hash of the token
   * @return optional token entity
   */
  Optional<PasswordResetTokenEntity> findByTokenHash(String tokenHash);

  /**
   * Deletes expired password reset tokens older than the provided timestamp.
   *
   * @param now current instant used to identify expired tokens
   */
  @Modifying
  @Transactional
  @Query("delete from PasswordResetTokenEntity t where t.expiresAt < :now")
  void deleteExpired(@Param("now") Instant now);
}
