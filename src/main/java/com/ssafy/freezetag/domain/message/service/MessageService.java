package com.ssafy.freezetag.domain.message.service;

import com.ssafy.freezetag.domain.message.entity.MessageRedis;
import com.ssafy.freezetag.domain.message.repository.MessageRedisRepository;
import com.ssafy.freezetag.domain.message.service.request.MessageRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MessageService {

    private final MessageRedisRepository messageRedisRepository;


    @Transactional
    public MessageRedis saveMessage(Long roomId, MessageRequestDto messageRequestDto){
        return messageRedisRepository.save(new MessageRedis(roomId, messageRequestDto.getMemberName(), messageRequestDto.getContent()));
    }

    public List<MessageRedis> getMessages(Long roomId){
        return messageRedisRepository.findAllByRoomId(roomId);
    }
}
