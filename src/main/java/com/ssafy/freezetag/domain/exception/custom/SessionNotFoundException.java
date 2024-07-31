package com.ssafy.freezetag.domain.exception.custom;

public class SessionNotFoundException extends RuntimeException {
    public SessionNotFoundException(String roomCode) {
        super("방 코드가 올바르지 않습니다. 현재 방코드 : " + roomCode);
    }
}
