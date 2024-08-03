package com.ssafy.freezetag.domain.oxresult.controller;

import com.ssafy.freezetag.domain.oxresult.service.request.OXModifyRequestDto;
import com.ssafy.freezetag.domain.oxresult.service.request.OXSaveRequestDto;
import com.ssafy.freezetag.global.argumentresolver.Login;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "나를 맞춰봐(OX게임)", description = "나를 맞춰봐 게임 관련 api")
public interface OXResultControllerSwagger {

    @Operation(summary = "OX게임 저장")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "저장 완료", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 입력", content = @Content(mediaType = "application/json"))
    })
    @PostMapping()
    ResponseEntity<?> saveOX(@Login Long memberId, @RequestBody List<OXSaveRequestDto> oxSaveRequestDtoList);


    @Operation(summary = "OX게임 단건 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "단건 조회 완료", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 입력", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{roomId}")
    ResponseEntity<?> getOX(@Login Long memberId, @PathVariable Long roomId);
    
    
    @Operation(summary = "OX게임 전체 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "전체 조회 완료", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 입력", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{roomId}/all")
    ResponseEntity<?> getOXs(@PathVariable Long roomId);
    
    
    @Operation(summary = "OX게임 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 완료", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 입력", content = @Content(mediaType = "application/json"))
    })
    @PatchMapping("/{roomId}/modify")
    ResponseEntity<?> modifyOX(@Login Long memberId, @PathVariable Long roomId, @RequestBody List<OXModifyRequestDto> oxModifyRequestDtoList);

    @Operation(summary = "OX게임 삭제(다음게임)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "삭제 완료", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 입력", content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{roomId}")
    ResponseEntity<?> deleteOXs(@PathVariable Long roomId);
}
