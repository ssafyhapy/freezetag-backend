package com.ssafy.freezetag.domain.result.repository;

import com.ssafy.freezetag.domain.result.entity.redis.IntroRedis;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IntroRedisRepository extends CrudRepository<IntroRedis, String> {
    List<IntroRedis> findByRoomId(Long roomId);
}
