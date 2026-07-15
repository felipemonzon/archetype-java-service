package com.moontech.archetype.infrastructure.exception.custom;

import java.io.Serial;

/**
 * Exception for data not found.
 *
 * @author Felipe Monzón
 * @since 2026-06-09
 */
public class NotDataFoundException extends RuntimeException {
  /** Serial. */
  @Serial private static final long serialVersionUID = -6450278167900735942L;

  /** Class constructor. */
  public NotDataFoundException() {
    super();
  }

  /**
   * Class constructor.
   *
   * @param message error message
   */
  public NotDataFoundException(String message) {
    super(message);
  }
}
