package com.ssafy.freezetag.domain.balanceresult.repository;

import com.ssafy.freezetag.domain.balanceresult.entity.BalanceQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceQuestionRepository extends JpaRepository<BalanceQuestion, Long> {
}
