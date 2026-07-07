package com.moontech.archetype.commons.constant;

/**
 * Constants for application.
 *
 * @author Felipe Monzón
 * @since 2026-06-09
 */
public abstract class ApiConstant {
  /** Constant for the comma symbol. */
  public static final String COMMA = ",";

  /** Constant for the UUID header. */
  public static final String HEADER_UUID = "uuid";

  /** Constant to show the start of the request. */
  public static final String START_REQUEST = "Inicia Llamado [{}]";

  /** Constant for the req.time key. */
  public static final String TIME_REQ_ATTRIBUTE = "req.time";

  /** Constant used as a key for the UUID header attribute. */
  public static final String UUID_MDC_LABEL = "mdc.uuid";

  /** Constant to show the request and response time. */
  public static final String TIME_ELAPSED_MESSAGE =
      "Time elapsed for request round trip [{}]: {} ms";

  /** Prefix for application properties. */
  public static final String PROPERTY_PREFIX = "api";

  /** Prefix for Swagger properties. */
  public static final String SWAGGER_PROPERTIES = "swagger";

  /** System user. */
  public static final String USER_SYSTEM = "System";

  /** Properties for mail. */
  public static final String PROPERTIES_MAIL = "api.mail";

  /** Private constructor. */
  private ApiConstant() {}
}