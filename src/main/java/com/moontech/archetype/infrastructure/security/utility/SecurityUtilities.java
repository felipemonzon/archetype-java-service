package com.moontech.archetype.infrastructure.security.utility;

import com.moontech.archetype.commons.constant.ApiConstant;
import com.moontech.archetype.infrastructure.security.constant.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Security utility.
 *
 * @author Felipe Monzón
 * @since 2206-06-29
 */
@UtilityClass
public class SecurityUtilities {
  /**
   * Encrypts the password.
   *
   * @param password password to encrypt
   * @return encrypted password
   */
  public static String passwordEncoder(final String password) {
    return new BCryptPasswordEncoder().encode(password);
  }

  /**
   * Generates the token with roles, issuer, date, expiration (8 h).
   *
   * @param authentication {@code Authentication}
   * @param signingKey key to decrypt or encrypt token
   * @param validity time to expired token
   * @return generated token
   */
  public static String generateToken(
      Authentication authentication, String signingKey, long validity) {
    final String authorities =
        authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));
    return Jwts.builder()
        .subject(authentication.getName())
        .claim(SecurityConstants.AUTHORITIES_KEY, authorities)
        .signWith(SecurityUtilities.getSigningKey(signingKey), Jwts.SIG.HS512)
        .issuedAt(new Date(System.currentTimeMillis()))
        .issuer(SecurityConstants.ISSUER_TOKEN)
        .expiration(new Date(System.currentTimeMillis() + validity))
        .compact();
  }

  /**
   * Gets the key for the jwt.
   *
   * @param secretKey secret key
   * @return key used in the jwt
   */
  public static SecretKey getSigningKey(String secretKey) {
    byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  /**
   * Gets the token from the header.
   *
   * @param authorizationHeader token provided in the header
   * @return token
   */
  public static String getTokenByHeader(final String authorizationHeader) {
    return authorizationHeader.replace(
        SecurityConstants.TOKEN_BEARER_PREFIX + StringUtils.SPACE, StringUtils.EMPTY);
  }

  /**
   * Gets the user from the token.
   *
   * @param token token received in the request
   * @param jwtKey token key
   * @return user
   */
  public static String getUserName(final String token, final String jwtKey) {
    return getClaimFromToken(token, jwtKey, Claims::getSubject);
  }

  /**
   * Gets a claim from the token.
   *
   * @param token token received in the request
   * @param jwtKey token key
   * @param claimsResolver function to resolve the claim
   * @param <T> type of the claim
   * @return the resolved claim
   */
  public static <T> T getClaimFromToken(
      String token, String jwtKey, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token, jwtKey);
    return claimsResolver.apply(claims);
  }

  /**
   * Gets all claims from the token.
   *
   * @param token token received in the request
   * @param jwtKey token key
   * @return all claims from the token
   */
  private static Claims getAllClaimsFromToken(String token, String jwtKey) {
    return Jwts.parser()
        .verifyWith(SecurityUtilities.getSigningKey(jwtKey))
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  /**
   * Validates the token.
   *
   * @param token token to validate
   * @param userDetails user details
   * @param jwtKey token key
   * @return true if the token is valid, false otherwise
   */
  public static Boolean validateToken(String token, UserDetails userDetails, String jwtKey) {
    final String username = getUserName(token, jwtKey);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token, jwtKey));
  }

  /**
   * Checks if the token is expired.
   *
   * @param token token to check
   * @param jwtKey token key
   * @return true if the token is expired, false otherwise
   */
  private static Boolean isTokenExpired(String token, String jwtKey) {
    final Date expiration = getClaimFromToken(token, jwtKey, Claims::getExpiration);
    return expiration.before(new Date());
  }

  /***
   * Gets authentication data.
   *
   * @param token token received in the request
   * @param userDetails {@link UserDetails}
   * @param jwtKey token key
   * @return {@link UsernamePasswordAuthenticationToken}
   */
  public static UsernamePasswordAuthenticationToken getAuthentication(
      final String token, final UserDetails userDetails, final String jwtKey) {
    final Claims claims = getAllClaimsFromToken(token, jwtKey);
    final Collection<SimpleGrantedAuthority> authorities =
        Arrays.stream(
                claims.get(SecurityConstants.AUTHORITIES_KEY).toString().split(ApiConstant.COMMA))
            .map(SimpleGrantedAuthority::new)
            .toList();
    return new UsernamePasswordAuthenticationToken(userDetails, StringUtils.EMPTY, authorities);
  }

  /**
   * Generates a refresh token for the given authentication.
   *
   * @param authentication the authentication object
   * @return the generated refresh token
   */
  public static String generateRefreshToken(
      Authentication authentication, String signingKey, long validity) {
    return SecurityUtilities.generateToken(authentication, signingKey, validity);
  }

  public static String addTokenToHeader(String accessToken) {
    return SecurityConstants.TOKEN_BEARER_PREFIX + StringUtils.SPACE + accessToken;
  }
}
