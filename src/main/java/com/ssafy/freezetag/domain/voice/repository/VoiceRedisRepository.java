package com.ssafy.freezetag.domain.voice.repository;

import com.ssafy.freezetag.domain.voice.entity.VoiceRedis;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface VoiceRedisRepository extends CrudRepository<VoiceRedis, String> {

    List<VoiceRedis> findAllByRoomId(Long roomId);
}
