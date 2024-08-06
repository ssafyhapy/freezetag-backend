package com.ssafy.freezetag.domain.oxresult.controller;

import com.ssafy.freezetag.domain.introresult.service.request.IntroSocketRequestDto;
import com.ssafy.freezetag.domain.introresult.service.response.IntroSocketResponseDto;
import com.ssafy.freezetag.domain.oxresult.service.OXSocketService;
import com.ssafy.freezetag.domain.oxresult.service.request.OXSocketRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class OXSocketController {

    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final OXSocketService oxSocketService;

    @MessageMapping("/ox/{roomId}/check")
    public void checkIntro(@DestinationVariable Long roomId, List<OXSocketRequestDto> oxSocketRequestDtos){
        oxSocketService.saveOX(roomId, oxSocketRequestDtos);

        if (oxSocketService.checkAllOX(roomId)) {
            IntroSocketResponseDto firstIntro = oxSocketService.getNextIntro(roomId);
            simpMessageSendingOperations.convertAndSend("/api/sub/intro/" + roomId + "/next", firstIntro);
        }
    }

    /**
     * /api/pub/intro/1/next
     */
    @MessageMapping("/ox/{roomId}/next")
    public void getNextIntro(@DestinationVariable Long roomId) {
        IntroSocketResponseDto nextIntro = introSocketService.getNextIntro(roomId);
        simpMessageSendingOperations.convertAndSend("/api/sub/intro/" + roomId + "/next", nextIntro);
    }
}
