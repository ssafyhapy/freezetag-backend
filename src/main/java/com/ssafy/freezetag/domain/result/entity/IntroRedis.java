package com.ssafy.freezetag.domain.result.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@RedisHash(value = "intro", timeToLive = 1800)
@AllArgsConstructor
public class IntroRedis {
    @Id
    private Long id;

    @Indexed
    private Long roomId;

    private String content;
}
