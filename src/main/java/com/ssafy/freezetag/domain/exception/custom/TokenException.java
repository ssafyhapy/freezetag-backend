package com.ssafy.freezetag.domain.exception.custom;

public class    TokenException extends RuntimeException {

    public TokenException(String errorCode) {
        super(errorCode);
    }
}