package com.ssafy.freezetag.domain.message.service.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MessageRequestDto {
    private String memberName;

    private String content;

    public MessageRequestDto(String memberName, String content) {
        this.memberName = memberName;
        this.content = content;
    }
}
