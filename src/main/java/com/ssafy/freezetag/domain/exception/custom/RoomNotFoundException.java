package com.ssafy.freezetag.domain.exception.custom;

public class RoomNotFoundException extends RuntimeException {
    public RoomNotFoundException() {
        super("해당 방이 존재하지 않습니다.");
    }
}
