package com.ssafy.freezetag.domain.room.service.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OpenviduResponseDto {
    // 이유는 모르겠는데 세션 아이디도 같이 보내줘야 한다고함
    private  String sessionId;
    private  String openviduToken;
}
