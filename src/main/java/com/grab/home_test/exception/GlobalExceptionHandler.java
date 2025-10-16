package com.grab.home_test.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

   @ExceptionHandler(DomainException.class)
   public ResponseEntity<Map<String, Object>> handleDomainException(DomainException ex) {
      Map<String, Object> response = new HashMap<>();
      response.put("error", ex.getErrorMessage());
      response.put("errorCode", ex.getErrorCode());

      return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
   }
}
