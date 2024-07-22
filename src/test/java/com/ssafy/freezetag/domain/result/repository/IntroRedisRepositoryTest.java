package com.ssafy.freezetag.domain.result.repository;

import com.ssafy.freezetag.domain.result.entity.IntroRedis;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IntroRedisRepositoryTest {

    @Autowired
    private IntroRedisRepository introRedisRepository;

    @Test
    @DisplayName("한줄 자기소개 redis 저장")
    public void save(){
        // given
        IntroRedis introRedis = new IntroRedis(1L, 100L, "나는 고민호입니다.");

        // when
        introRedisRepository.save(introRedis);
        IntroRedis foundIntroRedis = introRedisRepository.findById(1L).orElse(null);

        // then
        assertNotNull(foundIntroRedis, "Entity should be found");
        assertEquals(1L, foundIntroRedis.getId(), "ID should match");
        assertEquals(123L, foundIntroRedis.getRoomId(), "Room ID should match");
        assertEquals("Sample content", foundIntroRedis.getContent(), "Content should match");
    }
}