package com.ssafy.freezetag.domain.result.service.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OXSaveRequestDto {
    private final Long roomId;

    private final Long memberRoomId;

    private final String content;

    private final boolean answer;
}
