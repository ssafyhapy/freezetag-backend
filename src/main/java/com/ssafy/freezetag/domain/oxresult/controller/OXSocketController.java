package com.ssafy.freezetag.domain.oxresult.controller;

import com.ssafy.freezetag.domain.oxresult.service.OXSocketService;
import com.ssafy.freezetag.domain.oxresult.service.request.OXSocketNextRequestDto;
import com.ssafy.freezetag.domain.oxresult.service.request.OXSocketRequestDto;
import com.ssafy.freezetag.domain.oxresult.service.response.OXSocketNextResponseDto;
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
    public void checkOX(@DestinationVariable Long roomId, List<OXSocketRequestDto> oxSocketRequestDtos) {
        oxSocketService.saveOX(roomId, oxSocketRequestDtos);

        if (oxSocketService.checkAllOX(roomId)) {
            List<OXSocketResponseDto> firstOX = oxSocketService.getNextOX(roomId);
            simpMessageSendingOperations.convertAndSend("/api/sub/ox/" + roomId + "/next", firstOX);
        }
    }

    /*
    사용 보류 프론트와 논의 후 지우기!!!
    @MessageMapping("/ox/{roomId}/next")
    public void getNextOX(@DestinationVariable Long roomId) {
        List<OXSocketResponseDto> nextOX = oxSocketService.getNextOX(roomId);
        if (nextOX != null) {
            simpMessageSendingOperations.convertAndSend("/api/sub/ox/" + roomId + "/next", nextOX);
        }
    }*/

    @MessageMapping("/ox/{roomId}/next")
    public void getMyNextOx(@DestinationVariable Long roomId, OXSocketNextRequestDto oxSocketNextRequestDto) {
        int currentIndex = oxSocketNextRequestDto.getNowIndex();

        if (currentIndex < 2) {
            OXSocketNextResponseDto oxSocketNextResponseDto = new OXSocketNextResponseDto(oxSocketNextRequestDto.getMemberId(), currentIndex + 1);
            simpMessageSendingOperations.convertAndSend("/api/sub/ox/" + roomId + "/next", oxSocketNextResponseDto);
        } else if (currentIndex == 2) {
            List<OXSocketResponseDto> nextOX = oxSocketService.getNextOX(roomId);
            if (nextOX != null) {
                simpMessageSendingOperations.convertAndSend("/api/sub/ox/" + roomId + "/next", nextOX);
            }
        }
    }

}
