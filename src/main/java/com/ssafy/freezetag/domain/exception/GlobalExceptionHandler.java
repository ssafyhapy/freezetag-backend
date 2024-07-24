package com.ssafy.freezetag.domain.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(final RuntimeException e) {
        log.warn(e.getMessage());
        return ResponseEntity.internalServerError()
                .body(new ErrorResponse(false, e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(final RuntimeException e) {
        log.warn(e.getMessage());
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(false, e.getMessage()));
    }
}
