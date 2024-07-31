package com.ssafy.freezetag.domain.room.entity;

import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "room")
public class RoomRedis {
    @Id
    private String enterCode;
    private String sessionId;
    private Long roomId;

    public RoomRedis(final String enterCode, final String sessionId, final Long roomId) {
        this.enterCode = enterCode;
        this.sessionId = sessionId;
        this.roomId = roomId;
    }
}
