package com.ssafy.freezetag.domain.room.controller;

import com.ssafy.freezetag.domain.room.service.RoomService;
import com.ssafy.freezetag.domain.room.service.request.RoomCreateRequestDto;
import com.ssafy.freezetag.domain.room.service.response.RoomConnectResponseDto;
import com.ssafy.freezetag.global.argumentresolver.Login;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @PostMapping("/create")
    public ResponseEntity<?> createRoom(@Login Long memberId, @RequestBody RoomCreateRequestDto createRequestDto) {
        // 생성된 방 정보 (방 제목, 접속 코드 등) 을 DB에 엔티티로 저장
        RoomConnectResponseDto roomConnectResponseDto = roomService.createRoom(createRequestDto, memberId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Result<>(true, roomConnectResponseDto));
    }

    @PostMapping("/enter")
    public ResponseEntity<?> enterRoom(@Login Long memberId, @RequestParam String roomCode) {

        return ResponseEntity.ok(new Result<>(true, null));
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private boolean success;
        private T data;
    }
}
