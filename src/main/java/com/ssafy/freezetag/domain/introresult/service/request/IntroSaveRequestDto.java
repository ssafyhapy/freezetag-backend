package com.ssafy.freezetag.domain.introresult.service.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class IntroSaveRequestDto {
    private final Long roomId;

    private final String content;
}
