package com.ssafy.freezetag.domain.member.controller;

import com.ssafy.freezetag.domain.balanceresult.service.BalanceResultService;
import com.ssafy.freezetag.domain.member.service.request.MemberStateSocketRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MemberStateSocketController {

    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final BalanceResultService balanceResultService;

    /**
     * 게임 단계 확인 Topic
     * @param roomId
     * @param memberStateSocketRequestDto
     */
    @MessageMapping("/{roomId}/state")
    public void getNowState(@DestinationVariable Long roomId, MemberStateSocketRequestDto memberStateSocketRequestDto) {
        if(StringUtils.equals(memberStateSocketRequestDto.getMemberState(), "wrapup")){
            balanceResultService.deleteBalanceQuestion(roomId);
        }

        simpMessageSendingOperations.convertAndSend("/api/sub/" + roomId + "/state", memberStateSocketRequestDto);
        log.info("{}번 회원님이 {}상태로 성공적으로 넘어갔습니다.", roomId, memberStateSocketRequestDto.getMemberState());
    }

}
