package com.ssafy.freezetag.domain.message.service.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MessageResponseDto {
    private final String memberName;

    private final String content;

    private final LocalDateTime time;

    public MessageResponseDto(String memberName, String content, LocalDateTime time) {
        this.memberName = memberName;
        this.content = content;
        this.time = time;
    }
}
