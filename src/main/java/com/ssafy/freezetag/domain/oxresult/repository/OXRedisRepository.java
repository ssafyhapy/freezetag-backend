package com.ssafy.freezetag.domain.oxresult.repository;

import com.ssafy.freezetag.domain.oxresult.entity.OXRedis;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface OXRedisRepository extends CrudRepository<OXRedis, String> {
    List<OXRedis> findAllByRoomId(Long roomId);

    Optional<List<OXRedis>> findByMemberRoomId(Long memberRoomId);
}
