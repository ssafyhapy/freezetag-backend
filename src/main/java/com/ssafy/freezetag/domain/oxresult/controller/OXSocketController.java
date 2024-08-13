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
     * OX 작성(저장)
     * 모든 사람 작성 완료시 하나의 OX 리스트 반환
     * @param roomId
     * @param oxSocketRequestDtos
     */
    @MessageMapping("/ox/{roomId}/check")
    public void checkOX(@DestinationVariable Long roomId, List<OXSocketRequestDto> oxSocketRequestDtos) {
        oxSocketService.saveOX(roomId, oxSocketRequestDtos);

        if (oxSocketService.checkAllOX(roomId)) {
            List<OXSocketResponseDto> firstOX = oxSocketService.getNextOX(roomId);
            simpMessageSendingOperations.convertAndSend("/api/sub/ox/" + roomId + "/next", firstOX);
        }
    }

    /**
     * 다음 버튼 클릭
     * 현재 index가 0 or 1 경우 다음 index 반환
     * 현재 index가 2 경우 다음 OX 리스트 반환
     * @param roomId
     * @param oxSocketNextRequestDto
     */
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
