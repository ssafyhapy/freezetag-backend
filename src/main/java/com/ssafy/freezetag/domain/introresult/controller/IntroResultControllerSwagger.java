package com.ssafy.freezetag.domain.introresult.controller;

import com.ssafy.freezetag.domain.introresult.service.request.IntroModifyRequestDto;
import com.ssafy.freezetag.domain.introresult.service.request.IntroSaveRequestDto;
import com.ssafy.freezetag.global.argumentresolver.Login;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "한 줄 자기소개", description = "한 줄 자기소개 관련 api")
public interface IntroResultControllerSwagger {

    @Operation(summary = "한 줄 자기소개 저장")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "저장 완료", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 입력", content = @Content(mediaType = "application/json"))
    })
    @PostMapping()
    ResponseEntity<?> saveIntro(@Login Long memberId,  @RequestBody IntroSaveRequestDto introSaveRequestDto);

    @Operation(summary = "한 줄 자기소개 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 완료", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 입력", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/{roomId}/modify")
    ResponseEntity<?> modifyIntro(@Login Long memberId, @PathVariable Long roomId, @RequestBody IntroModifyRequestDto introModifyRequestDto);

    @Operation(summary = "한 줄 자기소개 단건 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 입력", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{roomId}")
    ResponseEntity<?> getIntro(@Login Long memberId, @PathVariable Long roomId);

    
    @Operation(summary = "한 줄 자기소개 전체조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "전체 조회 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 입력", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{roomId}/all")
    ResponseEntity<?> getIntros(@PathVariable Long roomId);

    @Operation(summary = "한 줄 자기소개 삭제(다음게임)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "삭제 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 입력", content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{roomId}")
    ResponseEntity<?> deleteIntros(@PathVariable Long roomId);
}
