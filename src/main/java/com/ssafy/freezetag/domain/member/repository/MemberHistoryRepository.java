package com.ssafy.freezetag.domain.member.repository;

import com.ssafy.freezetag.domain.member.entity.Member;
import com.ssafy.freezetag.domain.member.entity.MemberHistory;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberHistoryRepository extends JpaRepository<MemberHistory, Long> {
    @EntityGraph(attributePaths = {"member"})
    List<MemberHistory> findByMemberId(Long memberId);

    @Query("SELECT m FROM Member m " +
            "LEFT JOIN FETCH m.memberHistories mh " +
            "WHERE m.id = :memberId")
    Member findMemberWithHistories(Long memberId);


//    // history가 없을 수도 있으므로 RIGHT JOIN 해야 함
//    @Query("SELECT mh " +
//            "FROM MemberHistory mh " +
//            "RIGHT JOIN FETCH mh.member m " +
//            "WHERE m.id = :memberId")
//    List<MemberHistory> findAllByMemberIdWithFetchJoinHistory(Long memberId);

}
