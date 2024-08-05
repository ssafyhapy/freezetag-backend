package com.ssafy.freezetag.domain.introresult.service.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class IntroSaveResponseDto {
    private  String id;

    private  Long roomId;

    private  Long memberId;

    private  Long memberRoomId;

    private  String content;
}
