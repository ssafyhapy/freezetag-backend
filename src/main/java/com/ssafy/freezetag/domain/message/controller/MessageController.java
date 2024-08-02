package com.ssafy.freezetag.domain.message.controller;

import com.ssafy.freezetag.domain.message.entity.MessageRedis;
import com.ssafy.freezetag.domain.message.service.MessageService;
import com.ssafy.freezetag.domain.message.service.request.MessageRequestDto;
import com.ssafy.freezetag.domain.message.service.response.MessageResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MessageController {

    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final MessageService messageService;

    /**
     * "/pub/message/{roomId}" 경로로 클라이언트가 메시지를 보내면 해당 TOPIC을 구독 중인 사용자들에게 메시지 전달
     */
    @MessageMapping("/message/{roomId}")
    public void message(@DestinationVariable Long roomId, MessageRequestDto messageRequestDto){
        MessageRedis messageRedis = messageService.saveMessage(roomId, messageRequestDto);
        MessageResponseDto messageResponseDto = new MessageResponseDto(messageRedis.getMemberName(), messageRedis.getContent(), messageRedis.getCreatedDate());
        simpMessageSendingOperations.convertAndSend("/sub/" + roomId, messageResponseDto);

        log.info("{}번 방에 {}님이 {}를 입력하셨습니다.", roomId, messageRequestDto.getMemberName(), messageRequestDto.getContent());
    }
}
