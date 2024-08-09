package com.ssafy.freezetag.domain.message.service.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MessageRequestDto {
    private Long memberId;

    private String memberName;

    private String content;

    public MessageRequestDto(Long memberId, String memberName, String content) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.content = content;
    }
}
