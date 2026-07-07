package com.moontech.archetype.infrastructure.security.constant;

/**
 * Constants for service security configuration.
 *
 * @author Felipe Monzón
 * @since 2024-07-29
 */
public abstract class SecurityConstants {
  /** Authorization key. */
  public static final String AUTHORITIES_KEY = "CLAIM_TOKEN";

  /** Owner token. */
  public static final String ISSUER_TOKEN = "ISSUER";

  /** Bearer token. */
  public static final String TOKEN_BEARER_PREFIX = "Bearer";

  /** Refresh token header name. */
  public static final String REFRESH_TOKEN_HEADER_NAME = "refresh_token";

  /** Allows only administrator profile. */
  public static final String ADMIN_ALLOWED = "hasRole('ADMIN')";

  /** Allows administrator and customer profiles. */
  public static final String ADMIN_OR_CUSTOMER_ALLOWED = "hasRole('ADMIN') or hasRole('CUSTOMER')";

  /** Prefix for saving a role. */
  public static final String ROLE_PREFIX = "ROLE_";

  private SecurityConstants() {}
}
