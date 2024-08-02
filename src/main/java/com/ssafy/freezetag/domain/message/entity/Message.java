package com.ssafy.freezetag.domain.message.entity;

import com.ssafy.freezetag.domain.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message extends BaseEntity {

    private Long roomId;

    private Long memberId;

    private String content;

    public Message(Long roomId, Long memberId, String content) {
        this.roomId = roomId;
        this.memberId = memberId;
        this.content = content;
    }
}
