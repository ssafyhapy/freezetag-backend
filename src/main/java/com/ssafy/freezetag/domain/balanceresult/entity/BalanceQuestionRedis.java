package com.ssafy.freezetag.domain.balanceresult.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@RedisHash(value = "balance_question", timeToLive = 600)
@NoArgsConstructor
public class BalanceQuestionRedis {
    @Id
    private String id;

    @Indexed
    private Long roomId;

    private String optionFirst;

    private String optionSecond;

    public BalanceQuestionRedis(Long roomId, String optionFirst, String optionSecond) {
        this.roomId = roomId;
        this.optionFirst = optionFirst;
        this.optionSecond = optionSecond;
    }
}
