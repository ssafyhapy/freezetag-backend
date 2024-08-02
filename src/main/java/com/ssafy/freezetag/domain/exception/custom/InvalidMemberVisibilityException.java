package com.ssafy.freezetag.domain.exception.custom;

public class InvalidMemberVisibilityException extends RuntimeException
{
    public InvalidMemberVisibilityException(String errorCode) {
        super(errorCode);
    }
}
