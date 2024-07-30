package com.ssafy.freezetag.domain.oxresult.service.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OXModifyRequestDto {
    private final String id;

    private final String content;

    private final boolean answer;
}
