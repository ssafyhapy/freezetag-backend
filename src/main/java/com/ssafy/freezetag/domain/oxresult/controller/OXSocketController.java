package com.ssafy.freezetag.domain.oxresult.controller;

import com.ssafy.freezetag.domain.oxresult.service.OXSocketService;
import com.ssafy.freezetag.domain.oxresult.service.request.OXSocketRequestDto;
import com.ssafy.freezetag.domain.oxresult.service.response.OXSocketResponseDto;
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

    /**
     * /api/pub/ox/1/check
     */
    @MessageMapping("/ox/{roomId}/check")
    public void checkIntro(@DestinationVariable Long roomId, List<OXSocketRequestDto> oxSocketRequestDtos){
        oxSocketService.saveOX(roomId, oxSocketRequestDtos);

        if (oxSocketService.checkAllOX(roomId)) {
            List<OXSocketResponseDto> firstOX = oxSocketService.getNextOX(roomId);
            simpMessageSendingOperations.convertAndSend("/api/sub/ox/" + roomId + "/next", firstOX);
        }
    }

    /**
     * /api/pub/ox/1/next
     */
    @MessageMapping("/ox/{roomId}/next")
    public void getNextIntro(@DestinationVariable Long roomId) {
        List<OXSocketResponseDto> nextOX = oxSocketService.getNextOX(roomId);
        simpMessageSendingOperations.convertAndSend("/api/sub/ox/" + roomId + "/next", nextOX);
    }
}
