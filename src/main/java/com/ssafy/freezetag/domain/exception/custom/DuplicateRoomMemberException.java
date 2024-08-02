package com.ssafy.freezetag.domain.exception.custom;

public class DuplicateRoomMemberException extends RuntimeException {
    public DuplicateRoomMemberException() {
        super("이미 방에 존재하는 멤버입니다.");
    }
}
