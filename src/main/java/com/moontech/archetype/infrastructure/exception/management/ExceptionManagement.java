package com.moontech.archetype.infrastructure.exception.management;

import com.moontech.archetype.commons.constant.ApiConstant;
import com.moontech.archetype.commons.constant.ErrorConstant;
import com.moontech.archetype.infrastructure.exception.custom.BusinessException;
import com.moontech.archetype.infrastructure.exception.custom.ErrorResponse;
import com.moontech.archetype.infrastructure.exception.custom.ForbiddenException;
import com.moontech.archetype.infrastructure.exception.custom.NotDataFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

/**
 * Exception handler.
 *
 * @author Felipe Monzón
 * @since 2026-06-09
 */
@Slf4j
@RestControllerAdvice
public class ExceptionManagement {
  /** Enumerator for errors. */
  public enum ErrorType {
    ERROR,
    WARN,
    INVALID,
    FATAL
  }

  /**
   * Method to handle an exception of type {@link Exception}.
   *
   * @param request Http Servlet request object.
   * @param ex Received exception {@link Exception}
   * @return errorResponse {@link ErrorResponse} specific response for {@link Exception}.
   */
  @ExceptionHandler(Exception.class)
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorResponse generalException(Exception ex, WebRequest request) {
    ErrorResponse apiError =
        ErrorResponse.builder()
            .type(ErrorType.FATAL.name())
            .code(ErrorConstant.GENERIC_ERROR_CODE)
            .message(ErrorConstant.GENERIC_ERROR_MESSAGE)
            .moreInfo(ex.getMessage())
            .uuid(request.getHeader(ApiConstant.HEADER_UUID))
            .build();
    log.debug(apiError.toString());
    return apiError;
  }

  /**
   * Method to handle an exception of type {@link NotDataFoundException}.
   *
   * @param request Http Servlet request object.
   * @param ex Received exception {@link NotDataFoundException}
   * @return errorResponse {@link ErrorResponse} specific response for {@link
   *     NotDataFoundException}.
   */
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  @ExceptionHandler(NotDataFoundException.class)
  public ErrorResponse notDataFoundException(NotDataFoundException ex, WebRequest request) {
    ErrorResponse apiError =
        ErrorResponse.builder()
            .type(ErrorType.WARN.name())
            .code(ErrorConstant.RECORD_NOT_FOUND_CODE)
            .message(ErrorConstant.RECORD_NOT_FOUND_MESSAGE)
            .moreInfo(ex.getMessage())
            .uuid(request.getHeader(ApiConstant.HEADER_UUID))
            .build();
    log.debug(apiError.toString());
    return apiError;
  }

  /**
   * Method to handle an exception of type {@link HttpRequestMethodNotSupportedException}.
   *
   * @param request Http Servlet request object.
   * @param ex Received exception {@link HttpRequestMethodNotSupportedException}
   * @return errorResponse {@link ErrorResponse} specific response for {@link
   *     HttpRequestMethodNotSupportedException}.
   */
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  @ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
  public ErrorResponse resolveHttpRequestMethodNotSupportedException(
      HttpRequestMethodNotSupportedException ex, WebRequest request) {
    ErrorResponse apiError =
        ErrorResponse.builder()
            .type(ErrorType.ERROR.name())
            .code(ErrorConstant.BAD_REQUEST_CODE)
            .message(ex.getMessage())
            .uuid(request.getHeader(ApiConstant.HEADER_UUID))
            .build();
    log.debug(apiError.toString());
    return apiError;
  }

  /**
   * Method to handle an exception of type {@link HttpMediaTypeNotSupportedException}.
   *
   * @param request Http Servlet request object.
   * @param ex Received exception {@link HttpMediaTypeNotSupportedException}
   * @return errorResponse {@link ErrorResponse} specific response for {@link
   *     HttpMediaTypeNotSupportedException}.
   */
  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  @ResponseStatus(value = HttpStatus.UNSUPPORTED_MEDIA_TYPE)
  public ErrorResponse resolveHttpMediaTypeNotSupportedException(
      HttpMediaTypeNotSupportedException ex, WebRequest request) {
    ErrorResponse apiError =
        ErrorResponse.builder()
            .type(ErrorType.ERROR.name())
            .code(ErrorConstant.BAD_REQUEST_CODE)
            .message(ex.getMessage())
            .uuid(request.getHeader(ApiConstant.HEADER_UUID))
            .build();
    log.debug(apiError.toString());
    return apiError;
  }

  /**
   * Method to handle an exception of type {@link HttpMediaTypeNotAcceptableException}.
   *
   * @param request Http Servlet request object.
   * @param ex Received exception {@link HttpMediaTypeNotAcceptableException}
   * @return errorResponse {@link ErrorResponse} specific response for {@link
   *     HttpMediaTypeNotAcceptableException}.
   */
  @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
  @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
  public ErrorResponse resolveHttpMediaTypeNotAcceptableException(
      WebRequest request, HttpMediaTypeNotAcceptableException ex) {
    ErrorResponse apiError =
        ErrorResponse.builder()
            .type(ErrorType.INVALID.name())
            .code(ErrorConstant.BAD_REQUEST_CODE)
            .message(ex.getMessage())
            .uuid(request.getHeader(ApiConstant.HEADER_UUID))
            .build();
    log.debug(apiError.toString());
    return apiError;
  }

  /**
   * Method to handle an exception of type {@link MethodArgumentNotValidException}.
   *
   * @param request Http Servlet request object.
   * @param ex Received exception {@link MethodArgumentNotValidException}
   * @return errorResponse {@link ErrorResponse} specific response for {@link
   *     MethodArgumentNotValidException}.
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ErrorResponse resolveMethodArgumentNotValidException(
      MethodArgumentNotValidException ex, WebRequest request) {
    List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

    List<String> fields = fieldErrors.stream().map(FieldError::getField).toList();
    Map<String, List<String>> groupedErrors =
        fieldErrors.stream()
            .collect(
                Collectors.toMap(
                    FieldError::getDefaultMessage, f -> Collections.singletonList(f.getField())));

    ErrorResponse apiError =
        ErrorResponse.builder()
            .type(ErrorType.INVALID.name())
            .code(ErrorConstant.BAD_REQUEST_CODE)
            .message(groupedErrors.toString())
            .moreInfo(fields.toString())
            .uuid(request.getHeader(ApiConstant.HEADER_UUID))
            .build();
    log.debug(apiError.toString());
    return apiError;
  }

  /**
   * Method to handle an exception of type {@link BusinessException}.
   *
   * @param request Http Servlet request object.
   * @param ex Received exception {@link BusinessException}
   * @return errorResponse {@link ErrorResponse} specific response for {@link BusinessException}.
   */
  @ExceptionHandler(BusinessException.class)
  @ResponseStatus(value = HttpStatus.OK)
  public ErrorResponse resolveBusinessException(WebRequest request, BusinessException ex) {
    ErrorResponse apiError =
        ErrorResponse.builder()
            .type(ErrorType.INVALID.name())
            .code(ex.getCode())
            .message(ex.getMessage())
            .uuid(request.getHeader(ApiConstant.HEADER_UUID))
            .build();
    log.debug(apiError.toString());
    return apiError;
  }

  /**
   * Handles an exception of type {@code DataIntegrityViolationException} generated by SQL
   * exceptions.
   *
   * @param req Request
   * @param ex exception generated by JPA
   * @return specific response object for {@see DataIntegrityViolationException}
   */
  @ExceptionHandler(DataIntegrityViolationException.class)
  @ResponseStatus(value = HttpStatus.CONFLICT)
  public ErrorResponse resolveDataIntegrityViolationException(
      HttpServletRequest req, DataIntegrityViolationException ex) {
    String error = NestedExceptionUtils.getMostSpecificCause(ex).getMessage();

    String[] message = Arrays.stream(error.split(":")).map(String::trim).toArray(String[]::new);
    ErrorResponse errorResponse =
        ErrorResponse.builder()
            .type(ErrorType.ERROR.name())
            .code(ErrorConstant.GENERIC_ERROR_CODE)
            .message(message[0])
            .moreInfo(
                message[0].replace(ErrorConstant.PREFIX_DETAIL_MESSAGE, StringUtils.EMPTY).trim())
            .uuid(req.getHeader(ApiConstant.HEADER_UUID))
            .timestamp(ZonedDateTime.now())
            .build();
    log.error(errorResponse.toString());
    return errorResponse;
  }

  /**
   * Method to handle an exception of type {@link ForbiddenException}.
   *
   * @param request Http Servlet request object.
   * @param ex Received exception {@link ForbiddenException}
   * @return errorResponse {@link ErrorResponse} specific response for {@link ForbiddenException}.
   */
  @ExceptionHandler(ForbiddenException.class)
  @ResponseStatus(value = HttpStatus.FORBIDDEN)
  public ErrorResponse resolveForbiddenException(WebRequest request, ForbiddenException ex) {
    ErrorResponse apiError =
        ErrorResponse.builder()
            .type(ErrorType.INVALID.name())
            .code(ex.getCode())
            .message(ex.getMessage())
            .uuid(request.getHeader(ApiConstant.HEADER_UUID))
            .build();
    log.debug(apiError.toString());
    return apiError;
  }
}
