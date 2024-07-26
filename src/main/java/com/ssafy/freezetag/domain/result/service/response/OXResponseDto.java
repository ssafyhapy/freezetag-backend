package com.ssafy.freezetag.domain.result.service.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OXResponseDto {
    private final String id;

    private final String content;

    private final boolean answer;
}
