package com.ssafy.freezetag.domain.room.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssafy.freezetag.domain.room.service.request.RoomCreateRequestDto;
import com.ssafy.freezetag.global.argumentresolver.Login;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "방", description = "방 관련 api")
public interface RoomControllerSwagger {

    @Operation(summary = "방 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "방 생성 완료", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 입력", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/create")
    ResponseEntity<?> createRoom(@Login Long memberId, @RequestBody RoomCreateRequestDto createRequestDto);

    @Operation(summary = "방 입장")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "방 입장 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 입력", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/enter")
    ResponseEntity<?> enterRoom(@Login Long memberId, @RequestParam String roomCode) throws JsonProcessingException;

    @Operation(summary = "방 중도 퇴장")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "방 퇴장 완료", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "방 퇴장 실패", content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{roomId}/exit")
    ResponseEntity<?> enterRoom(@Login Long memberId, @PathVariable Long roomId);

    @Operation(summary = "방 결과 레포트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "방 결과 레포트 조회 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 입력", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{roomId}/report")
    ResponseEntity<?> getRoomReport(@PathVariable Long roomId);
}
