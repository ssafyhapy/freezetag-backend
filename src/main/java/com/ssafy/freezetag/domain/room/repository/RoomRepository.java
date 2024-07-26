package com.ssafy.freezetag.domain.room.repository;

import com.ssafy.freezetag.domain.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
