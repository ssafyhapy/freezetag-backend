package com.ssafy.freezetag.domain.message.service.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class MessageResponseDto {
    private final String memberName;

    private final String content;

    private final LocalDateTime time;
}
