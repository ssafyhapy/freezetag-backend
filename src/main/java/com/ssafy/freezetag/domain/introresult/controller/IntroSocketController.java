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
     * /api/pub/intro/1/check
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
     * /api/pub/intro/1/next
     */
    @MessageMapping("/intro/{roomId}/next")
    public void getNextIntro(@DestinationVariable Long roomId) {
        IntroSocketResponseDto nextIntro = introSocketService.getNextIntro(roomId);
        if(nextIntro != null){
            simpMessageSendingOperations.convertAndSend("/api/sub/intro/" + roomId + "/next", nextIntro);
        }
    }
}
