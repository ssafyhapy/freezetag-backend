package com.ssafy.freezetag.domain.room.repository;

import com.ssafy.freezetag.domain.room.entity.Room;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    @EntityGraph(attributePaths = {"memberRooms", "memberRooms.member"})
    Optional<Room> findById(Long id);

    @EntityGraph(attributePaths = {"memberRooms", "memberRooms.member"})
    List<Room> findByCreatedDateBefore(LocalDate createdDate);
}
