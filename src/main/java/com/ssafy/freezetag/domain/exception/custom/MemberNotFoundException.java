package com.ssafy.freezetag.domain.exception.custom;

import com.ssafy.freezetag.domain.member.entity.Member;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(String errorCode) {
        super(errorCode);
    }
}
