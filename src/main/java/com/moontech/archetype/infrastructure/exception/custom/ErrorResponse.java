package com.moontech.archetype.infrastructure.exception.custom;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import com.moontech.archetype.commons.constant.FormatConstant;
import java.time.ZonedDateTime;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

/**
 * Error response.
 *
 * @author Felipe Monzón
 * @since 2026-06-09
 */
@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
  /** Error type. */
  private String type;

  /** Error code. */
  @Builder.Default private int code = 0;

  /** Error details. */
  @Builder.Default private String message = StringUtils.EMPTY;

  /** Additional error information. */
  @Builder.Default private String moreInfo = StringUtils.EMPTY;

  /** Request UUID header. */
  @Builder.Default private String uuid = StringUtils.EMPTY;

  /** Date and time when the error occurred. */
  @Builder.Default
  @JsonSerialize(using = ZonedDateTimeSerializer.class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = FormatConstant.ERROR_DATE_PATTERN)
  private ZonedDateTime timestamp = ZonedDateTime.now();
}
