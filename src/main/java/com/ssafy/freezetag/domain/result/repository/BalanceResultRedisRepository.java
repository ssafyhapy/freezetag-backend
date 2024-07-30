package com.ssafy.freezetag.domain.result.repository;

import com.ssafy.freezetag.domain.result.entity.redis.BalanceResultRedis;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BalanceResultRedisRepository extends CrudRepository<BalanceResultRedis, String> {
    List<BalanceResultRedis> findAllByBalanceQuestionId(String id);

    void deleteAllByBalanceQuestionId(String id);
}
