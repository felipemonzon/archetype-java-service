package com.moontech.archetype.infrastructure.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request payload to initiate a password reset for a user.
 *
 * <p>Contains the user's email and an optional frontend URL to construct the reset link.
 *
 * @author Felipe Monzón
 * @since 2026-07-21
 */
@Getter
@Setter
@NoArgsConstructor
public class PasswordResetRequest {
  @Email @NotBlank private String email;

  /** Optional frontend url to build the reset link (e.g. https://app.example.com/reset-password) */
  private String frontendUrl;
}
