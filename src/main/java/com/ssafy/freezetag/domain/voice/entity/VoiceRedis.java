package com.ssafy.freezetag.domain.voice.entity;

import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@RedisHash(value = "voice")
public class VoiceRedis {

    @Id
    private String id;

    @Indexed
    private Long roomId;

    private String memberName;

    private String content;

    private LocalDateTime createdDate;

    public VoiceRedis(Long roomId, String memberName, String content) {
        this.id = UUID.randomUUID().toString();
        this.roomId = roomId;
        this.memberName = memberName;
        this.content = content;
        this.createdDate = LocalDateTime.now();
    }
}
