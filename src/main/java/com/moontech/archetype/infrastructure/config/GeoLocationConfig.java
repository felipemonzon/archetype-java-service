package com.moontech.archetype.infrastructure.config;

import com.maxmind.geoip2.DatabaseReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermissions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * Configuration for GeoIP database. For GeoLite2 database, the path can be specified in
 * application.properties as geoip.db-path. There database is downloaded from MaxMind, accepted
 * license, and downloads a database (Not upload a database if your license is not permitted).
 *
 * @author Felipe Monzón
 * @since 2026-06-09
 */
@Configuration
@RequiredArgsConstructor
public class GeoLocationConfig {
  private static final String GE_OIP_TMP_NAME_PREFIX = "GeoLite2-City-h";

  private static final String GE_OIP_TMP_NAME = GE_OIP_TMP_NAME_PREFIX + System.currentTimeMillis();

  private static final String GE_OIP_TMP_NAME_SUFFIX = ".mmdb";

  /** Path to the GeoIP database file. */
  @Value("${geoip.db-path:classpath:geolocation/GeoLite2-City.mmdb}")
  private String dbPath;

  /**
   * Creates a new DatabaseReader instance for the GeoIP database.
   *
   * @return DatabaseReader
   * @throws IOException if the database file cannot be read
   */
  @Bean(destroyMethod = "close")
  public DatabaseReader geoIpDatabaseReader() throws IOException {
    String res = dbPath.substring("classpath:".length());
    ClassPathResource cp = new ClassPathResource(res);
    if (!cp.exists()) throw new FileNotFoundException("GeoIP DB not found in classpath: " + res);
    File tmp =
        Files.createTempFile(
                GE_OIP_TMP_NAME,
                GE_OIP_TMP_NAME_SUFFIX,
                PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rw-------")))
            .toFile();
    tmp.deleteOnExit();
    try (InputStream in = cp.getInputStream();
        FileOutputStream out = new FileOutputStream(tmp)) {
      in.transferTo(out);
    }
    return new DatabaseReader.Builder(tmp).build();
  }
}
