package com.ssafy.freezetag.domain.message.service.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MessageRequestDto {
    private final String memberName;

    private final String content;
}
