package com.ssafy.freezetag.domain.room.repository;

import com.ssafy.freezetag.domain.room.entity.Room;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface RoomRepository extends JpaRepository<Room, Long> {
    @EntityGraph(attributePaths = {"memberRooms", "memberRooms.member"})
    Room findRoomWithMembers(@Param("roomId") Long roomId);
}
