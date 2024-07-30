package com.ssafy.freezetag.domain.balanceresult.repository;

import com.ssafy.freezetag.domain.balanceresult.entity.BalanceQuestionRedis;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BalanceQuestionRedisRepository extends CrudRepository<BalanceQuestionRedis, String> {
    List<BalanceQuestionRedis> findAllByRoomId(Long roomId);
}
