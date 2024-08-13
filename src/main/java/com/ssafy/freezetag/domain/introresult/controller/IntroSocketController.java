package com.ssafy.freezetag.domain.introresult.controller;

import com.ssafy.freezetag.domain.introresult.service.IntroSocketService;
import com.ssafy.freezetag.domain.introresult.service.request.IntroSocketRequestDto;
import com.ssafy.freezetag.domain.introresult.service.response.IntroSocketResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class IntroSocketController {

    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final IntroSocketService introSocketService;

    /**
     * 자기소개 작성(저장)
     * 모든 사람 작성 완료시 하나의 자기소개 반환
     * @param roomId
     * @param introSocketRequestDto
     */
    @MessageMapping("/intro/{roomId}/check")
    public void checkIntro(@DestinationVariable Long roomId, IntroSocketRequestDto introSocketRequestDto){
        introSocketService.saveIntro(roomId, introSocketRequestDto);

        if (introSocketService.checkAllIntro(roomId)) {
            IntroSocketResponseDto firstIntro = introSocketService.getNextIntro(roomId);
            simpMessageSendingOperations.convertAndSend("/api/sub/intro/" + roomId + "/next", firstIntro);
        }
    }

    /**
     * 다음 버튼 클릭
     * 다른 회원의 자기소개 반환
     * @param roomId
     */
    @MessageMapping("/intro/{roomId}/next")
    public void getNextIntro(@DestinationVariable Long roomId) {
        IntroSocketResponseDto nextIntro = introSocketService.getNextIntro(roomId);
        if(nextIntro != null){
            simpMessageSendingOperations.convertAndSend("/api/sub/intro/" + roomId + "/next", nextIntro);
        }
    }
}
