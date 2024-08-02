package com.ssafy.freezetag.domain.introresult.service.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class IntroResponseDto {
    private final String id;

    private final Long memberId;

    private final String content;
}
