package com.ssafy.freezetag.domain.message.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@RedisHash(value = "message", timeToLive = 600)
public class MessageRedis {
    @Id
    private String id;

    @Indexed
    private Long roomId;

    private String memberName;

    private String content;

    private LocalDateTime createdDate;

    public MessageRedis(Long roomId, String memberName, String content) {
        this.id = UUID.randomUUID().toString();
        this.roomId = roomId;
        this.memberName = memberName;
        this.content = content;
        this.createdDate = LocalDateTime.now();
    }
}
