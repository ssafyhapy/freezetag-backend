package com.ssafy.freezetag.domain.result.repository;

import com.ssafy.freezetag.domain.result.entity.redis.BalanceQuestionRedis;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BalanceQuestionRedisRepository extends CrudRepository<BalanceQuestionRedis, String> {
    List<BalanceQuestionRedis> findAllByRoomId(Long roomId);
}
