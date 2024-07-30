package com.ssafy.freezetag.domain.balanceresult.repository;

import com.ssafy.freezetag.domain.balanceresult.entity.BalanceResultRedis;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BalanceResultRedisRepository extends CrudRepository<BalanceResultRedis, String> {
    List<BalanceResultRedis> findAllByBalanceQuestionId(String id);

    void deleteAllByBalanceQuestionId(String id);
}
