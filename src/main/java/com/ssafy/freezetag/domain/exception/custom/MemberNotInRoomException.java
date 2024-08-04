package com.ssafy.freezetag.domain.exception.custom;

public class MemberNotInRoomException extends RuntimeException {
    public MemberNotInRoomException() {
        super("해당 유저는 현재 방에 존재하지 않습니다.");
    }
}
