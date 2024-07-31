package com.ssafy.freezetag.domain.exception.custom;

public class RoomFullException extends RuntimeException {
    public RoomFullException() {
        super("방 인원수가 가득 차서 입장할 수 없습니다.");
    }
}

