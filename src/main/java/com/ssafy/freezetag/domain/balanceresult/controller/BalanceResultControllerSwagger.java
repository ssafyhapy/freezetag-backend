package com.ssafy.freezetag.domain.balanceresult.controller;

import com.ssafy.freezetag.domain.balanceresult.service.request.BalanceQuestionRequestDto;
import com.ssafy.freezetag.domain.balanceresult.service.request.BalanceQuestionSaveRequestDto;
import com.ssafy.freezetag.domain.balanceresult.service.request.BalanceResultSaveRequestDto;
import com.ssafy.freezetag.global.argumentresolver.Login;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "밸런스 게임", description = "밸런스 게임 관련 api")
public interface BalanceResultControllerSwagger {

    @Operation(summary = "GPT에게 질문 가져오기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 입력", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/question")
    ResponseEntity<?> getBalanceQuestion(@RequestBody BalanceQuestionRequestDto balanceQuestionRequestDto);

    @Operation(summary = "질문 확정(저장)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "질문 저장 완료", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 입력", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/question")
    ResponseEntity<?> saveBalanceQuestion(@RequestBody BalanceQuestionSaveRequestDto balanceQuestionSaveRequestDto);

    @Operation(summary = "질문에 대한 선택 저장")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "선택 저장 완료", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 입력", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/selection")
    ResponseEntity<?> saveBalanceResult(@Login Long memberId, @RequestBody BalanceResultSaveRequestDto balanceResultSaveRequestDto);

    @Operation(summary = "질문 및 선택 삭제(다음게임)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "삭제 완료", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 입력", content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/question/{roomId}")
    ResponseEntity<?> deleteBalanceQuestion(@PathVariable Long roomId);
}
