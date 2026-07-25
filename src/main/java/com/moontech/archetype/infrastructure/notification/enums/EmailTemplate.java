package com.moontech.archetype.infrastructure.notification.enums;

import lombok.Getter;

/**
 * Email template types supported by the notification service.
 *
 * @author Felipe Monzón
 * @since 2026-07-21
 */
@Getter
public enum EmailTemplate {
  /** Email template for forgotten password reset. */
  FORGOT_PASSWORD("forgot_password"),
  /** Email template for password changed confirmation. */
  RESET_PASSWORD("reset_password");

  /** Template code identifier. */
  private final String code;

  /**
   * Constructs an EmailTemplate with the given code.
   *
   * @param code the template code identifier
   */
  EmailTemplate(String code) {
    this.code = code;
  }
}
