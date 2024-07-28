package com.ssafy.freezetag.global.exception;

public class TokenException extends RuntimeException {

    private String errorCode;
    public TokenException(String errorCode) {
        this.errorCode = errorCode;
    }
}