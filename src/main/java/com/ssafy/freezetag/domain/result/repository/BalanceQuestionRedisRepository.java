package com.ssafy.freezetag.domain.result.repository;

import com.ssafy.freezetag.domain.result.entity.redis.BalanceQuestionRedis;
import org.springframework.data.repository.CrudRepository;

public interface BalanceQuestionRedisRepository extends CrudRepository<BalanceQuestionRedis, String> {
}
