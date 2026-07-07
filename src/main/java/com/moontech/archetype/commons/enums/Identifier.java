package com.moontech.archetype.commons.enums;

import lombok.Getter;

/**
 * Enum de identificadores.
 *
 * @author Felipe Monzón
 * @since 2026-06-09
 */
@Getter
public enum Identifier {
  USERS("USR");

  /** Código del identificador. */
  private final String code;

  /**
   * Constructor del enum
   *
   * @param code código
   */
  Identifier(String code) {
    this.code = code;
  }
}
