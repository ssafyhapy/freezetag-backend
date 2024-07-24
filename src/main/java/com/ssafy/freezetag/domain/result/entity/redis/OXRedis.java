package com.ssafy.freezetag.domain.result.entity.redis;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.UUID;

@Getter
@RedisHash(value = "ox", timeToLive = 600)
public class OXRedis {
    @Id
    private String id;

    @Indexed
    private Long roomId;

    private Long memberRoomId;

    private String content;

    private Boolean answer;

    public OXRedis(Long roomId, Long memberRoomId, String content, Boolean answer) {
        this.id = UUID.randomUUID().toString();
        this.roomId = roomId;
        this.memberRoomId = memberRoomId;
        this.content = content;
        this.answer = answer;
    }

    public void update(String newContent, Boolean newAnswer){
        this.content = newContent;
        this.answer = newAnswer;
    }
}
