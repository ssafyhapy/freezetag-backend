package com.ssafy.freezetag.domain.voice.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Voice{

    private Long roomId;

    private Long memberId;

    private String content;

    public Voice(Long roomId, Long memberId, String content) {
        this.roomId = roomId;
        this.memberId = memberId;
        this.content = content;
    }
}
