package com.multimarketing.taskrestapi.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(ApiException.class)
        public ResponseEntity<?> handleException(ApiException ex) {

            return new ResponseEntity<>(ErrorResponse
                    .builder()
                    .error(ex.getMessage())
                    .build(),
                    ex.getHttpStatus()
            );
        }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        return new ResponseEntity<>("Data integrity violation: " + ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
