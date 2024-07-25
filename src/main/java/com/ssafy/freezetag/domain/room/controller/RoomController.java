package com.ssafy.freezetag.domain.room.controller;

import com.ssafy.freezetag.domain.room.service.OpenviduService;
import com.ssafy.freezetag.domain.room.service.RoomService;
import com.ssafy.freezetag.domain.room.service.request.OpenviduResponseDto;
import com.ssafy.freezetag.domain.room.service.request.RoomCreateRequestDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
public class RoomController {
    private final OpenviduService openviduService;
    private final RoomService roomService;

    @PostMapping("/enter")
    public ResponseEntity<?> enterRoom(@RequestBody RoomCreateRequestDto createRequestDto, HttpServletRequest request) {
        Cookie cookie = request.getCookies()[0];
        String jwtToken = cookie.getAttribute("Authorization");

        // TODO: 사용자 인증 로직 수행
        // TODO: 실제 사용자 아이디를 JWT Token 에서 가져와야함
        Long userId = 1L;

        // 생성된 방 정보 (방 제목, 접속 코드 등) 을 DB에 엔티티로 저장
        roomService.createRoom(createRequestDto, userId);

        OpenviduResponseDto openviduResponseDto = openviduService.createRoom();
        return ResponseEntity.ok(new Result<>(true, openviduResponseDto));
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private boolean success;
        private T data;
    }
}
