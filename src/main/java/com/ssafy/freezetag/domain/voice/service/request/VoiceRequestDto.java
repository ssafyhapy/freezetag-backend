package com.ssafy.freezetag.domain.voice.service.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VoiceRequestDto {

    private Long memberId;

    private String memberName;

    private String content;

    public VoiceRequestDto(Long memberId, String memberName, String content) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.content = content;
    }

}
