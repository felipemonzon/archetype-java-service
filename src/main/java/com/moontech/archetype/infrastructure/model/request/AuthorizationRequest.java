package com.moontech.archetype.infrastructure.model.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.moontech.archetype.commons.constant.FormatConstant;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * Authenticate user properties.
 *
 * @author Felipe Monzón
 * @since 2026-06-10
 */
@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AuthorizationRequest implements Serializable {
  /** Serial. */
  @Serial private static final long serialVersionUID = 1L;

  /** Username property. */
  @NotEmpty
  @Pattern(regexp = FormatConstant.ONLY_LETTERS_PATTERN)
  private String username;

  /** Password. */
  @NotEmpty
  @Pattern(regexp = FormatConstant.PAW_PATTERN)
  private String password;
}
