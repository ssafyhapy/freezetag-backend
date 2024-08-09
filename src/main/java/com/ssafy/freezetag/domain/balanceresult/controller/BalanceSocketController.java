package com.ssafy.freezetag.domain.balanceresult.controller;

import com.ssafy.freezetag.domain.balanceresult.entity.BalanceQuestionRedis;
import com.ssafy.freezetag.domain.balanceresult.entity.BalanceResultRedis;
import com.ssafy.freezetag.domain.balanceresult.service.BalanceResultService;
import com.ssafy.freezetag.domain.balanceresult.service.request.BalanceQuestionRequestDto;
import com.ssafy.freezetag.domain.balanceresult.service.request.BalanceQuestionSaveRequestDto;
import com.ssafy.freezetag.domain.balanceresult.service.request.BalanceResultSaveRequestDto;
import com.ssafy.freezetag.domain.balanceresult.service.response.BalanceQuestionResponseDto;
import com.ssafy.freezetag.domain.balanceresult.service.response.BalanceResultSaveResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
@MessageMapping("/balance")
public class BalanceSocketController {

    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final BalanceResultService balanceResultService;

    /**
     * /api/pub/balance/1/get-question
     * 방장만 주제 리롤 가능
     */
    @MessageMapping("/{roomId}/get-question")
    public void getBalanceQuestion(@DestinationVariable Long roomId,
                                   BalanceQuestionRequestDto balanceQuestionRequestDto){
        log.info("{}번 방에서 {} 모임 목적을 입력했습니다", roomId, balanceQuestionRequestDto.getPurpose());
        BalanceQuestionResponseDto question = balanceResultService.getQuestion(balanceQuestionRequestDto);
        simpMessageSendingOperations.convertAndSend("/api/sub/balance/" + roomId + "/get-question", question);
    }

    /**
     * /api/pub/balance/1/save-question
     * 방장만 주제 확정 가능
     */
    @MessageMapping("/{roomId}/save-question")
    public void getBalanceQuestion(@DestinationVariable Long roomId,
                                   BalanceQuestionSaveRequestDto balanceQuestionRequestDto){
        log.info("{}번 방에서 {} OR {} 밸런스 주제를 확정했습니다.", roomId, balanceQuestionRequestDto.getOptionFirst(), balanceQuestionRequestDto.getOptionSecond());
        BalanceQuestionRedis balanceQuestionRedis = balanceResultService.saveBalanceQuestion(roomId, balanceQuestionRequestDto);
        simpMessageSendingOperations.convertAndSend("/api/sub/balance/" + roomId + "/save-question", balanceQuestionRedis);
    }


    // 타이머 종료시 각 사용자들이 A or B 중 무엇을 선택했는지 소켓으로 전송 그리고 Redis 저장
    @MessageMapping("/{roomId}/selection")
    public void saveBalanceSelection(@DestinationVariable Long roomId, BalanceResultSaveRequestDto balanceResultSaveRequestDto){
        log.info("{}번 방에서 {}번 회원님이 {}를 선택했습니다.", roomId, balanceResultSaveRequestDto.getMemberId(), balanceResultSaveRequestDto.getBalanceResultSelectedOption());
        BalanceResultRedis balanceResultRedis = balanceResultService.saveBalanceResult(balanceResultSaveRequestDto);
        BalanceResultSaveResponseDto balanceResultSaveResponseDto = new BalanceResultSaveResponseDto(balanceResultRedis.getMemberId(),
                balanceResultRedis.getBalanceResultSelectedOption());
        simpMessageSendingOperations.convertAndSend("/api/sub/balance/" + roomId + "/selection", balanceResultSaveResponseDto);
    }

}
