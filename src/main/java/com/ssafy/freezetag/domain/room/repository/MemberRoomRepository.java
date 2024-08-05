package com.ssafy.freezetag.domain.room.repository;

import com.ssafy.freezetag.domain.room.entity.MemberRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRoomRepository extends JpaRepository<MemberRoom, Long> {
    Optional<MemberRoom> findByMemberIdAndRoomId(Long memberId, Long roomId);

    void deleteByMember_IdAndRoom_Id(Long memberId, Long roomId);
}
