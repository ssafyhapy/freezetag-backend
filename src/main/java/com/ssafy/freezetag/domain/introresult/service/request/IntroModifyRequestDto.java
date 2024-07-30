package com.ssafy.freezetag.domain.introresult.service.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class IntroModifyRequestDto {
    private final String id;    // redis 식별키

    private final String content;
}
