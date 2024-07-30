package com.ssafy.freezetag.domain.exception.custom;

public class OpenviduTokenException extends RuntimeException{
    public OpenviduTokenException() {
        super("Openvidu 토큰 생성에 실패했습니다.");
    }
}
