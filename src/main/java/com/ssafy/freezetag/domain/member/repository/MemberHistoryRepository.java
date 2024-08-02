package com.ssafy.freezetag.domain.member.repository;

import com.ssafy.freezetag.domain.member.entity.MemberHistory;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberHistoryRepository extends JpaRepository<MemberHistory, Long> {
    @EntityGraph(attributePaths = {"member"})
    List<MemberHistory> findByMemberId(Long memberId);
}
