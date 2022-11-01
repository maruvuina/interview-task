package com.example.interview.handler;

import javax.validation.ConstraintViolationException;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@RequiredArgsConstructor
@ControllerAdvice
public class ErrorHandlingControllerAdvice {

    @ExceptionHandler({ConstraintViolationException.class, MethodArgumentNotValidException.class,
            InvalidFormatException.class, JsonMappingException.class})
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException ex) {
        log.error(ex.getLocalizedMessage());
        return new ResponseEntity<>(ex.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
    }
}
