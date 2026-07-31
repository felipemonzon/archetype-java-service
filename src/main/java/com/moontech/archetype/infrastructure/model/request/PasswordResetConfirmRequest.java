package com.moontech.archetype.infrastructure.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request payload to confirm a password reset using a token. Contains the reset token and the new
 * desired password.
 *
 * @author Felipe Monzón
 * @since 2026-07-21
 */
@Getter
@Setter
@NoArgsConstructor
public class PasswordResetConfirmRequest {
  @NotBlank private String token;

  @NotBlank private String password;

  private String ip;

  private String ipLocation;

  private String device;

  private String currentDate;
}
