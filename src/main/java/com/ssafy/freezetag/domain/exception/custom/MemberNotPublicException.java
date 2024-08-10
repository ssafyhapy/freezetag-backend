package com.ssafy.freezetag.domain.exception.custom;

public class MemberNotPublicException extends RuntimeException{

    public MemberNotPublicException(String errorCode) {
        super(errorCode);
    }
}
