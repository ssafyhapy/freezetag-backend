package com.ssafy.freezetag.domain.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.ssafy.freezetag.domain.common.CommonResponse.failure;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleBusinessException(final RuntimeException e) {
        log.warn(e.getMessage());
        return ResponseEntity.internalServerError()
                .body(failure(e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(final RuntimeException e) {
        log.warn(e.getMessage());
        return ResponseEntity.badRequest()
                .body(failure(e.getMessage()));
    }

    /*
        OAuth2 과정에서 인증 에러가 발생하면 에러 처리하는 로직
     */
    @ExceptionHandler(OAuth2AuthenticationException.class)
    public ResponseEntity<?> handleOAuth2AuthenticationException(final OAuth2AuthenticationException e) {
        log.warn("OAuth2 authentication error: {}", e.getMessage());
        return ResponseEntity.status(401)
                .body(failure(e.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(final RuntimeException e) {
        log.warn(e.getMessage());
        return ResponseEntity.status(404)
                .body(failure(e.getMessage()));
    }
}
