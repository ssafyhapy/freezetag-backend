package com.ssafy.freezetag.domain.member.controller;

import com.ssafy.freezetag.domain.member.service.request.MemberStateSocketRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MemberStateSocketController {

    private final SimpMessageSendingOperations simpMessageSendingOperations;

    /**
     * /api/pub/1/state
     */
    @MessageMapping("/{roomId}/state")
    public void getNowState(@DestinationVariable Long roomId, MemberStateSocketRequestDto memberStateSocketRequestDto) {

        log.info("{}번 회원님이 {}상태로 넘어가도록 알림을 보냈습니다.", roomId, memberStateSocketRequestDto.getMemberState());
        simpMessageSendingOperations.convertAndSend("/api/sub/" + roomId + "/state", memberStateSocketRequestDto);
        log.info("{}번 회원님이 {}상태로 성공적으로 넘어갔습니다.", roomId, memberStateSocketRequestDto.getMemberState());
    }

}
