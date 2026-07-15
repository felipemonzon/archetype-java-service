package com.moontech.archetype.commons.constant;

/**
 * Class with constants for formats.
 *
 * @author Felipe Monzón
 * @since 2026-06-09
 */
public class FormatConstant {
  /** Error response output format. */
  public static final String ERROR_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

  /** Format for letters only */
  public static final String ONLY_LETTERS_PATTERN = "^[A-Za-z\\s]+$";

  /** Format for numbers only */
  public static final String ONLY_NUMBERS_PATTERN = "^[0-9]+$";

  /** Format for numbers and letters only */
  public static final String ONLY_NUMBERS_AND_LETTERS_PATTERN = "^[A-Za-z0-9\\s]+$";

  /** Format for passwords. */
  public static final String PAW_PATTERN = "^[A-Za-z0-9@#$%-&+=]+$";

  /** Format for numbers and letters only */
  public static final String USERNAME_PATTERN = "^[A-Za-z0-9\\s\\.\\_\\-]+$";

  /** Private constructor. */
  private FormatConstant() {}
}
