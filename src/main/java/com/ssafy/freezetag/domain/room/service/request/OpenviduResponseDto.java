package com.ssafy.freezetag.domain.room.service.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OpenviduResponseDto {
    // 이유는 모르겠는데 세션 아이디도 같이 보내줘야 한다고함
    private final String sessionId;
    private final String openviduToken;
}
