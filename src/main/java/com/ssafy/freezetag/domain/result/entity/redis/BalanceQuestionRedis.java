package com.ssafy.freezetag.domain.result.entity.redis;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@RedisHash(value = "balance", timeToLive = 600)
@NoArgsConstructor
public class BalanceQuestionRedis {
    @Id
    private String id;

    @Indexed
    private Long roomId;

    private String optionFirst;

    private String optionSecond;
}
