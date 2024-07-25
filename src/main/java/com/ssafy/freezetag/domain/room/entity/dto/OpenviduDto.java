package com.ssafy.freezetag.domain.room.entity.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OpenviduDto {
    // 이유는 모르겠는데 세션 아이디도 같이 보내줘야 한다고함
    private String sessionId;
    private String token;

    @Builder
    public OpenviduDto(final String sessionId, final String token) {
        this.sessionId = sessionId;
        this.token = token;
    }
}
