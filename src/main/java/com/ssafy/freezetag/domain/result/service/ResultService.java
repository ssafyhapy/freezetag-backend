package com.ssafy.freezetag.domain.result.service;

import com.ssafy.freezetag.domain.result.entity.IntroRedis;
import com.ssafy.freezetag.domain.result.repository.IntroRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResultService {

    private final IntroRedisRepository introRedisRepository;

    public void save(Long id, Long roomId, String content) {
        introRedisRepository.save(new IntroRedis(id, roomId, content));
    }
}
