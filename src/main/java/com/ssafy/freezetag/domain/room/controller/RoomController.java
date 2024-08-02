package com.ssafy.freezetag.domain.room.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssafy.freezetag.domain.room.service.RoomService;
import com.ssafy.freezetag.domain.room.service.request.RoomCreateRequestDto;
import com.ssafy.freezetag.domain.room.service.response.RoomConnectResponseDto;
import com.ssafy.freezetag.global.argumentresolver.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.ssafy.freezetag.domain.common.CommonResponse.success;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @PostMapping("/create")
    public ResponseEntity<?> createRoom(@Login Long memberId, @RequestBody RoomCreateRequestDto createRequestDto){
        // 생성된 방 정보 (방 제목, 접속 코드 등) 을 DB에 엔티티로 저장
        RoomConnectResponseDto roomConnectResponseDto = roomService.createRoom(createRequestDto, memberId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(success(roomConnectResponseDto));
    }

    @PostMapping("/enter")
    public ResponseEntity<?> enterRoom(@Login Long memberId, @RequestParam String roomCode) throws JsonProcessingException {
        RoomConnectResponseDto responseDto = roomService.enterRoom(roomCode, memberId);
        return ResponseEntity.ok(success(responseDto));
    }

}
