package com.ssafy.freezetag.domain.room.service;

import com.ssafy.freezetag.domain.exception.custom.SessionNotFoundException;
import com.ssafy.freezetag.domain.room.entity.RoomRedis;
import com.ssafy.freezetag.domain.room.repository.RoomRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomRedisService {
    private final RoomRedisRepository roomRedisRepository;

    public void saveRoomInfo(String enterCode, String sessionId, Long roomId) {
        RoomRedis roomRedis = new RoomRedis(enterCode, sessionId, roomId);
        roomRedisRepository.save(roomRedis);
    }

    public RoomRedis fetchRoomInfo(String enterCode) {
        return roomRedisRepository.findById(enterCode).orElseThrow(() -> new SessionNotFoundException(enterCode));
    }
}
