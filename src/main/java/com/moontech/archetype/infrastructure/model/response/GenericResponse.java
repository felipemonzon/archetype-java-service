package com.moontech.archetype.infrastructure.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Generic API response wrapper used by simple endpoints.
 *
 * @author Felipe Monzón
 * @since 2026-07-21
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenericResponse {
  /** Properties for message. */
  private String message;
}
