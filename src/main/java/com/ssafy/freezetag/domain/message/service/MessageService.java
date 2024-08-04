package com.ssafy.freezetag.domain.message.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.freezetag.domain.message.entity.MessageRedis;
import com.ssafy.freezetag.domain.message.repository.MessageRedisRepository;
import com.ssafy.freezetag.domain.message.service.request.MessageRequestDto;
import com.ssafy.freezetag.domain.room.service.response.RoomUserJoinEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MessageService {

    private final MessageRedisRepository messageRedisRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    @Transactional
    public MessageRedis saveMessage(Long roomId, MessageRequestDto messageRequestDto) {
        return messageRedisRepository.save(new MessageRedis(roomId, messageRequestDto.getMemberName(), messageRequestDto.getContent()));
    }

    public List<MessageRedis> getMessages(Long roomId) {
        return messageRedisRepository.findAllByRoomId(roomId);
    }

    public void sendUserJoinEvent(Long roomId, RoomUserJoinEvent roomUserJoinEvent) throws JsonProcessingException {
        String joinEvent = objectMapper.writeValueAsString(roomUserJoinEvent);
        messagingTemplate.convertAndSend("/sub/room/" + roomId, joinEvent);
    }
}
