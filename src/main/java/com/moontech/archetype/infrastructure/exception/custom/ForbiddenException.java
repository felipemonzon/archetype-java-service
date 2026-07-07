package com.moontech.archetype.infrastructure.exception.custom;

import lombok.Getter;

import java.io.Serial;

/**
 * Exception thrown for invalid credentials.
 *
 * @author Felipe Monzón
 * @since 2026-06-09
 */
@Getter
public class ForbiddenException extends RuntimeException {
  /** Serial. */
  @Serial private static final long serialVersionUID = -6450278167900735942L;
  /**
   * Error code.
   */
  private final int code;

  /**
   * Class constructor.
   *
   * @param code error code
   * @param message error message
   */
  public ForbiddenException(int code, String message) {
    super(message);
    this.code = code;
  }
}