package com.ssafy.freezetag.domain.room.repository;

import com.ssafy.freezetag.domain.room.entity.RoomRedis;
import org.springframework.data.repository.CrudRepository;

public interface RoomRedisRepository extends CrudRepository<RoomRedis, String> {
}
