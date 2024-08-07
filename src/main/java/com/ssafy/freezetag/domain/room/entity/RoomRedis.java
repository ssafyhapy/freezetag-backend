package com.ssafy.freezetag.domain.room.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "room", timeToLive = 3600)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
