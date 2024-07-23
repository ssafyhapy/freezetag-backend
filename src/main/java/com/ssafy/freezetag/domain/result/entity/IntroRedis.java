package com.ssafy.freezetag.domain.result.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.UUID;

@Getter
@RedisHash(value = "intro", timeToLive = 1800)
@NoArgsConstructor
public class IntroRedis {
    @Id
    private String id;

    @Indexed
    private Long roomId;

    private String content;

    public IntroRedis(Long roomId, String content) {
        this.id = UUID.randomUUID().toString();
        this.roomId = roomId;
        this.content = content;
    }

    public String updateContent(String newContent){
        this.content = newContent;
        return this.content;
    }
}
