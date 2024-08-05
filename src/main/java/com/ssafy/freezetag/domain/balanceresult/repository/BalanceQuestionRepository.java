package com.ssafy.freezetag.domain.balanceresult.repository;

import com.ssafy.freezetag.domain.balanceresult.entity.BalanceQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BalanceQuestionRepository extends JpaRepository<BalanceQuestion, Long> {
    List<BalanceQuestion> findAllByRoomId(Long roomId);
}
