package com.ssafy.freezetag.domain.introresult.entity;

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

    @Indexed
    private Long memberId;

    @Indexed
    private Long memberRoomId;

    private String content;

    public IntroRedis(Long roomId, Long memberId, Long memberRoomId, String content) {
        this.id = UUID.randomUUID().toString();
        this.roomId = roomId;
        this.memberId = memberId;
        this.memberRoomId = memberRoomId;
        this.content = content;
    }

    public void update(String newContent){
        this.content = newContent;
    }
}
