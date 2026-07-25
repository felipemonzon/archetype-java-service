package com.moontech.archetype.commons.utilities;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import com.moontech.archetype.commons.constant.ApiConstant;
import com.moontech.archetype.commons.constant.FormatConstant;
import com.moontech.archetype.infrastructure.config.GeoLocationConfig;
import jakarta.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;

/**
 * Utilities.
 *
 * @author Felipe Monzón
 * @since 2026-06-09
 */
@UtilityClass
public class Utilities {
  /**
   * Get the current pagel.
   *
   * @param pageable pagination
   * @return current page
   */
  public static int getCurrentPage(Pageable pageable) {
    int page = pageable.getPageNumber();
    if (pageable.getPageNumber() != 0) {
      page -= 1;
    }
    return page;
  }

  /**
   * Generates the ID to be saved.
   *
   * @param prefix prefix of the random identifier
   * @return generated identifier
   */
  public static String generateRandomId(String prefix) {
    return prefix
        + RandomStringUtils.random(17, 0, 0, Boolean.TRUE, Boolean.TRUE, null, new SecureRandom());
  }

  /** Gets current date-time in yyyy-MM-dd HH:mm:ss format. */
  public static String getCurrentDateTime() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FormatConstant.DATE_TIME_PATTERN);
    return LocalDateTime.now(ZoneId.systemDefault()).format(formatter);
  }

  /** Utility to extract client IP from HTTP request. */
  public static String getClientIp(HttpServletRequest request) {
    String ip = request.getHeader("X-Forwarded-For");
    if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getRemoteAddr();
    }
    // Si X-Forwarded-For contiene múltiples IPs, toma la primera
    if (ip != null && ip.contains(",")) {
      ip = ip.split(",")[0].trim();
    }
    return ip;
  }

  /**
   * Extracts device/browser info from the User-Agent header
   *
   * @param request HTTP request.
   * @return device info.
   */
  public static String getDeviceInfo(HttpServletRequest request) {
    return request.getHeader(HttpHeaders.USER_AGENT);
  }

  /** Gets country and city from IP using MaxMind GeoIP2. */
  public static String getGeoLocation(String ipAddress, GeoLocationConfig geoLocationConfig) {
    try (DatabaseReader reader = geoLocationConfig.geoIpDatabaseReader()) {
      InetAddress inetAddress = InetAddress.getByName(ipAddress);
      String languageCode = LocaleContextHolder.getLocale().getLanguage();
      CityResponse response = reader.city(inetAddress);
      return response
              .getCountry()
              .getNames()
              .getOrDefault(languageCode, response.getCountry().getName())
          + ApiConstant.COMMA
          + StringUtils.SPACE
          + response.getCity().getNames().getOrDefault(languageCode, response.getCity().getName());
    } catch (Exception e) {
      return "Unknown";
    }
  }
}
