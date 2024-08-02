package com.ssafy.freezetag.domain.oxresult.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.UUID;

@Getter
@RedisHash(value = "ox", timeToLive = 600)
@NoArgsConstructor
public class OXRedis {
    @Id
    private String id;

    @Indexed
    private Long roomId;

    @Indexed
    private Long memberId;

    @Indexed
    private Long memberRoomId;

    private String content;

    private Boolean answer;

    public OXRedis(Long roomId, Long memberId, Long memberRoomId, String content, Boolean answer) {
        this.id = UUID.randomUUID().toString();
        this.roomId = roomId;
        this.memberId = memberId;
        this.memberRoomId = memberRoomId;
        this.content = content;
        this.answer = answer;
    }

    public void update(String newContent, Boolean newAnswer){
        this.content = newContent;
        this.answer = newAnswer;
    }
}
