package com.ssafy.freezetag.domain.introresult.repository;

import com.ssafy.freezetag.domain.introresult.entity.IntroRedis;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface IntroRedisRepository extends CrudRepository<IntroRedis, String> {
    List<IntroRedis> findAllByRoomId(Long roomId);

    Optional<IntroRedis> findByMemberRoomId(Long memberRoomId);
}
