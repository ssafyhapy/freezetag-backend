package com.ssafy.freezetag.domain.balanceresult.entity;

import com.ssafy.freezetag.domain.oxresult.entity.SelectedOption;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.UUID;

@Getter
@RedisHash(value = "balance_result", timeToLive = 600)
@NoArgsConstructor
public class BalanceResultRedis {
    @Id
    private String id;

    // BalanceQuestion id -> 식별키
    @Indexed
    private String balanceQuestionId;

    private Long memberId;

    private SelectedOption balanceResultSelectedOption;

    public BalanceResultRedis(String balanceQuestionId, Long memberId, SelectedOption balanceResultSelectedOption) {
        this.id = UUID.randomUUID().toString();
        this.balanceQuestionId = balanceQuestionId;
        this.memberId = memberId;
        this.balanceResultSelectedOption = balanceResultSelectedOption;
    }
}
