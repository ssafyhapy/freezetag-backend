package com.ssafy.freezetag.domain.oauth2;

public class TokenException extends RuntimeException {

    private String errorCode;
    public TokenException(String errorCode) {
        this.errorCode = errorCode;
    }
}