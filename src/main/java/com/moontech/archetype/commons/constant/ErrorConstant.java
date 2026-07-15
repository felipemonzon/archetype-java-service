package com.moontech.archetype.commons.constant;

/**
 * Constants for application errors.
 *
 * @author Felipe Monzón
 * @since 2026-06-09
 */
public class ErrorConstant {
  /** Generic code. */
  public static final Integer GENERIC_ERROR_CODE = 9009;

  /** Generic message. */
  public static final String GENERIC_ERROR_MESSAGE = "An unknown error occurred";

  /** Code for bad request. */
  public static final Integer BAD_REQUEST_CODE = 9002;

  /** Code for data not found. */
  public static final Integer RECORD_NOT_FOUND_CODE = 9003;

  /** Message for data not found. */
  public static final String RECORD_NOT_FOUND_MESSAGE = "Record not found";

  /** Code for invalid credentials. */
  public static final int INVALID_CREDENTIAL_USER_CODE = 9000;

  /** Message for incorrect username or password. */
  public static final String INVALID_CREDENTIAL_USER_MESSAGE = "Invalid username/password";

  /** Code for invalid token. */
  public static final int INVALID_TOKEN_CODE = 9001;

  /** Message for invalid token. */
  public static final String INVALID_TOKEN_MESSAGE = "Access Denied";

  /** Prefix for details. */
  public static final String PREFIX_DETAIL_MESSAGE = "Detail";

  /** Error code for existing data. */
  public static final int DATA_EXIST_CODE = 9007;

  /** Error code for non-existing data. */
  public static final int DATA_NOT_EXIST = 9008;

  /** Private constructor. */
  private ErrorConstant() {}
}
