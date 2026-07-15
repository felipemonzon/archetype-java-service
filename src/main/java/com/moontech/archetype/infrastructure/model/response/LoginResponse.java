package com.moontech.archetype.infrastructure.model.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.moontech.archetype.commons.enums.Genre;
import com.moontech.archetype.commons.enums.Status;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Class to return user data after login.
 *
 * @author Felipe Monzón
 * @since 2026-06-10
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LoginResponse implements Serializable {
  /** User identifier. */
  private String id;

  /** Username. */
  private String username;

  /** First name property. */
  private String firstName;

  /** Last name property. */
  private String lastName;

  /** Phone property. */
  private String phone;

  /** User email. */
  private String email;

  /** Genre property. */
  private Genre genre;

  /** Full name. */
  private String displayName;

  /** Enterprise identifier. */
  private Long enterpriseId;

  /** Enterprise name. */
  private String enterpriseName;

  /** Employee status. */
  private Status status;
}
