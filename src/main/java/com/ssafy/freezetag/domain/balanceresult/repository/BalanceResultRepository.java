package com.ssafy.freezetag.domain.balanceresult.repository;

import com.ssafy.freezetag.domain.balanceresult.entity.BalanceResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BalanceResultRepository extends JpaRepository<BalanceResult, Long> {
    List<BalanceResult> findAllByBalanceQuestionId(Long balanceQuestionId);
}
