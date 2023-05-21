package com.tenetmind.app.adapter.in;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
abstract class AbstractControllerAdvice {

  protected static void logWebException(Exception e, HttpStatus httpStatus) {
    String errorMessage = "Returning error response with code: " + httpStatus.value();
    if (httpStatus.series() == SERVER_ERROR) {
      log.error(errorMessage, e);
    } else {
      log.warn(errorMessage, e);
    }
  }

  @ExceptionHandler({
    MissingServletRequestPartException.class,
    MissingServletRequestParameterException.class,
    TypeMismatchException.class,
    MethodArgumentTypeMismatchException.class,
    HttpMessageNotReadableException.class,
    MissingRequestHeaderException.class
  })
  @ResponseStatus(BAD_REQUEST)
  ErrorResponse handleBadRequest(Exception e) {
    log.warn("Returning 400 Bad Request", e);
    return new ErrorResponse(e);
  }

  /**
   * BindException and MethodArgumentNotValidException have been separated because they require
   * special handling
   */
  @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
  @ResponseStatus(BAD_REQUEST)
  ErrorResponse handleBadRequest(BindException e) {
    log.warn("Returning 400 Bad Request", e);
    return new ErrorResponse(e);
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  @ResponseStatus(NOT_FOUND)
  ErrorResponse handleNotFound(Exception e) {
    log.warn("Returning 404 Not Found", e);
    return new ErrorResponse(e);
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  @ResponseStatus(METHOD_NOT_ALLOWED)
  ErrorResponse handleMethodNotAllowed(Exception e) {
    log.warn("Returning 405 Method Not Allowed", e);
    return new ErrorResponse(e);
  }

  @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
  @ResponseStatus(NOT_ACCEPTABLE)
  ErrorResponse handleNotAcceptable(Exception e) {
    log.warn("Returning 406 Not Acceptable", e);
    return new ErrorResponse(e);
  }

  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  @ResponseStatus(UNSUPPORTED_MEDIA_TYPE)
  ErrorResponse handleUnsupportedMediaType(Exception e) {
    log.warn("Returning 415 Unsupported Media Type", e);
    return new ErrorResponse(e);
  }

  @ExceptionHandler(Throwable.class)
  @ResponseStatus(INTERNAL_SERVER_ERROR)
  ErrorResponse handleAll(Throwable t) {
    log.error("Returning 500 Internal Server Error", t);
    return new ErrorResponse(t);
  }

  record ErrorResponse(String errorMessage) {
    ErrorResponse(Throwable e) {
      this(e.getMessage());
    }
  }
}
