package com.ssafy.freezetag.domain.member.repository;

import com.ssafy.freezetag.domain.member.entity.Member;
import com.ssafy.freezetag.domain.member.entity.MemberHistory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberHistoryRepository extends JpaRepository<MemberHistory, Long> {
    @EntityGraph(attributePaths = {"member"})
    List<MemberHistory> findByMemberId(Long memberId);

    @Query("SELECT m FROM Member m " +
            "LEFT JOIN FETCH m.memberHistories mh " +
            "WHERE m.id = :memberId")
    Optional<Member> findMemberWithHistories(Long memberId);

}
