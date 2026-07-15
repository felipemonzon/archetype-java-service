package com.moontech.archetype.configuration;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Test configuration for MySQL using Testcontainers. This class sets up a static, reusable MySQL
 * container for integration tests and dynamically registers its connection properties with the
 * Spring context.
 *
 * @author Felipe Monzón
 * @since 2026-07-13
 */
@Testcontainers
public abstract class MysqlBaseConfigurationTest {

  /** Docker image name for MySQL. */
  private static final DockerImageName MYSQL_IMAGE = DockerImageName.parse("mysql:latest");

  /** Static MySQL container instance. */
  private static final MySQLContainer mysqlContainer;

  static {
    mysqlContainer =
        new MySQLContainer(MYSQL_IMAGE)
            .withDatabaseName("archetypedb")
            .withUsername("root")
            .withPassword("root")
            .withReuse(Boolean.TRUE); // Enable container reuse for faster test execution
    mysqlContainer.start();
  }

  /**
   * Registers dynamic properties for the MySQL test container. These properties override the
   * default datasource configuration for tests.
   *
   * @param registry the {@link DynamicPropertyRegistry} to add properties to
   */
  @DynamicPropertySource
  static void registerMySQLProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
    registry.add("spring.datasource.username", mysqlContainer::getUsername);
    registry.add("spring.datasource.password", mysqlContainer::getPassword);
  }
}
