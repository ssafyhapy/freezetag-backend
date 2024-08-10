package com.ssafy.freezetag.domain.room.repository;

import com.ssafy.freezetag.domain.room.entity.MemberRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberRoomRepository extends JpaRepository<MemberRoom, Long> {
    Optional<MemberRoom> findByMemberIdAndRoomId(Long memberId, Long roomId);
    void deleteByMemberIdAndRoomId(Long memberId, Long roomId);
    List<MemberRoom> findAllByRoomId(Long roomId);
    @Query("SELECT mr " +
            "FROM MemberRoom mr " +
            "JOIN FETCH mr.room r " +
            "WHERE mr.member.id = :memberId")
    List<MemberRoom> findAllByMemberIdWithFetchJoin(Long memberId);
}
