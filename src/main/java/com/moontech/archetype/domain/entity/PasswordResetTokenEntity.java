package com.moontech.archetype.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Entity representing a password reset token stored in the database.
 *
 * <p>Only the token hash is persisted for security. Tokens have an expiration and a used flag.
 *
 * @author Felipe Monzón
 * @since 2026-07-21
 */
@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@Table(name = "password_reset_tokens")
@EntityListeners(AuditingEntityListener.class)
public class PasswordResetTokenEntity {
  /** Primary key identifier */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  /** SHA-256 hash of the password reset token (never store the plain token). */
  @Column(name = "token_hash", nullable = false, length = 128, unique = true)
  private String tokenHash;

  /** Owning user for the token */
  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private UserEntity user;

  /** Expiration timestamp for the token */
  @Column(name = "expires_at", nullable = false)
  private Instant expiresAt;

  /** Flag indicating whether the token was already used */
  @Column(name = "used", nullable = false)
  private Boolean used = Boolean.FALSE;
}
