package com.moontech.archetype.infrastructure.exception.custom;

import java.io.Serial;
import java.util.Collections;
import java.util.List;
import lombok.Getter;

/**
 * Exception for bad requests.
 *
 * @author Felipe Monzón
 * @since 2026-06-09
 */
@Getter
public class BadRequestException extends RuntimeException {
  /** Auto-generated UID for class versioning. */
  @Serial private static final long serialVersionUID = 8925303792177335247L;

  /** List of incorrect fields in the request. */
  private final List<String> badFields;

  /**
   * Class constructor.
   *
   * @param message exception message thrown by bad request.
   * @param badFields list of fields that caused the exception.
   */
  public BadRequestException(String message, List<String> badFields) {
    super(message);
    this.badFields = Collections.unmodifiableList(badFields);
  }
}
