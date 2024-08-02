package com.ssafy.freezetag.domain.message.repository;

import com.ssafy.freezetag.domain.message.entity.MessageRedis;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRedisRepository extends CrudRepository<MessageRedis, String> {
    List<MessageRedis> findAllByRoomId(Long roomId);
}
