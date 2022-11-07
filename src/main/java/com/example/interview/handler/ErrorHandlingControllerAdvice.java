package com.example.interview.handler;

import java.time.Instant;
import java.util.Arrays;

import javax.validation.ConstraintViolationException;

import com.example.interview.exception.BookException;
import com.example.interview.exception.JsonIOException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ErrorHandlingControllerAdvice {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleBookException(BookException ex) {
        return handleBadRequestException(ex);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleJsonIOException(JsonIOException ex) {
        return handleBadRequestException(ex);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleInvalidFormatException(InvalidFormatException ex) {
        return handleCommonException(ex);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        return handleCommonException(ex);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return handleCommonException(ex);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleJsonMappingException(JsonMappingException ex) {
        return handleCommonException(ex);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleWebExchangeBindException(WebExchangeBindException ex) {
        return handleBadRequestException(ex);
    }

    private ResponseEntity<ErrorResponse> handleBadRequestException(Exception ex) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .timestamp(Instant.now())
                        .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                        .message(ex.getMessage())
                        .trace(Arrays.toString(ex.getStackTrace()))
                        .build(),
                HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ErrorResponse> handleCommonException(Exception ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .timestamp(Instant.now())
                        .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                        .message(ex.getMessage())
                        .trace(Arrays.toString(ex.getStackTrace()))
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
