package com.ssafy.freezetag.domain.result.entity.redis;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.UUID;

@Getter
@RedisHash(value = "intro", timeToLive = 600)
@NoArgsConstructor
public class IntroRedis {
    @Id
    private String id;

    @Indexed
    private Long roomId;

    private Long memberRoomId;

    private String content;

    public IntroRedis(Long roomId, Long memberRoomId, String content) {
        this.id = UUID.randomUUID().toString();
        this.roomId = roomId;
        this.memberRoomId = memberRoomId;
        this.content = content;
    }

    public void update(String newContent){
        this.content = newContent;
    }
}
