package com.ssafy.freezetag.domain.room.repository;

import com.ssafy.freezetag.domain.room.entity.MemberRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRoomRepository extends JpaRepository<MemberRoom, Long> {
}
