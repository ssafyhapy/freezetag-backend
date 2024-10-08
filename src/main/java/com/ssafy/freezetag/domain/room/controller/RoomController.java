package com.ssafy.freezetag.domain.room.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssafy.freezetag.domain.member.service.MemberService;
import com.ssafy.freezetag.domain.room.service.MemoryBoxService;
import com.ssafy.freezetag.domain.room.service.RoomService;
import com.ssafy.freezetag.domain.room.service.request.RoomCreateRequestDto;
import com.ssafy.freezetag.domain.room.service.response.RoomConnectResponseDto;
import com.ssafy.freezetag.domain.room.service.response.RoomReportResponseDto;
import com.ssafy.freezetag.global.argumentresolver.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.ssafy.freezetag.domain.common.CommonResponse.success;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
public class RoomController implements RoomControllerSwagger {
    private final RoomService roomService;
    private final MemberService memberService;
    private final MemoryBoxService memoryBoxService;

    @PostMapping("/create")
    public ResponseEntity<?> createRoom(@Login Long memberId, @RequestBody RoomCreateRequestDto createRequestDto) {
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

    @DeleteMapping("/{roomId}/exit")
    public ResponseEntity<?> enterRoom(@Login Long memberId, @PathVariable Long roomId) {
        roomService.exitRoom(roomId, memberId);
        return ResponseEntity.noContent()
                .build();
    }

    @GetMapping("/{roomId}/report")
    public ResponseEntity<?> getRoomReport(@PathVariable Long roomId) {
        RoomReportResponseDto roomReport = roomService.getRoomReport(roomId);

        return ResponseEntity.ok()
                .body(success(roomReport));
    }

    @PostMapping("/{roomId}/memoryBox/before")
    public ResponseEntity<?> createBeforeMemoryPicture(@PathVariable("roomId") Long roomId, MultipartFile image) throws IOException {
        memoryBoxService.uploadBeforeMemoryImage(roomId, image);
        return ResponseEntity.noContent()
                .build();
    }

    @PostMapping("/{roomId}/memoryBox/after")
    public ResponseEntity<?> createAfterMemoryPicture(@PathVariable("roomId") Long roomId, MultipartFile image) throws IOException {
        memoryBoxService.uploadAfterMemoryImage(roomId, image);
        return ResponseEntity.noContent()
                .build();
    }


}
