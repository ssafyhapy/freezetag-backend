package com.ssafy.freezetag.domain.balanceresult.controller;

import com.ssafy.freezetag.domain.balanceresult.entity.BalanceQuestionRedis;
import com.ssafy.freezetag.domain.balanceresult.service.BalanceResultService;
import com.ssafy.freezetag.domain.balanceresult.service.request.BalanceQuestionRequestDto;
import com.ssafy.freezetag.domain.balanceresult.service.request.BalanceQuestionSaveRequestDto;
import com.ssafy.freezetag.domain.balanceresult.service.request.BalanceResultSaveRequestDto;
import com.ssafy.freezetag.domain.balanceresult.service.response.BalanceQuestionResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class BalanceSocketController {

    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final BalanceResultService balanceResultService;

    /**
     * /api/pub/balance/1/get-question
     * 방장만 주제 리롤 가능
     */
    @MessageMapping("/balance/{roomId}/get-question")
    public void getBalanceQuestion(@DestinationVariable Long roomId,
                                   BalanceQuestionRequestDto balanceQuestionRequestDto){
        BalanceQuestionResponseDto question = balanceResultService.getQuestion(balanceQuestionRequestDto);
        simpMessageSendingOperations.convertAndSend("/api/sub/balance/" + roomId + "/get-question", question);
    }

    /**
     * /api/pub/balance/1/save-question
     * 방장만 주제 확정 가능
     */
    @MessageMapping("/balance/{roomId}/save-question")
    public void getBalanceQuestion(@DestinationVariable Long roomId,
                                   BalanceQuestionSaveRequestDto balanceQuestionRequestDto){
        BalanceQuestionRedis balanceQuestionRedis = balanceResultService.saveBalanceQuestion(roomId, balanceQuestionRequestDto);
        simpMessageSendingOperations.convertAndSend("/api/sub/balance/" + roomId + "/save-question", balanceQuestionRedis);
    }


    // 타이머 종료시 각 사용자들이 A or B 중 무엇을 선택했는지 소켓으로 전송 그리고 Redis 저장
    @MessageMapping("/balance/{roomId}/selection")
    public void saveBalanceSelection(@DestinationVariable Long roomId, BalanceResultSaveRequestDto balanceResultSaveRequestDto){

    }

}
