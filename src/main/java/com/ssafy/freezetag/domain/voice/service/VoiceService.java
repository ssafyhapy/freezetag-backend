package com.ssafy.freezetag.domain.voice.service;

import com.ssafy.freezetag.domain.voice.entity.VoiceRedis;
import com.ssafy.freezetag.domain.voice.repository.VoiceRedisRepository;
import com.ssafy.freezetag.domain.voice.service.request.VoiceRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class VoiceService {

    private final VoiceRedisRepository voiceRedisRepository;

    @Transactional
    public VoiceRedis saveVoice(Long roomId, VoiceRequestDto voiceRequestDto) {
        return voiceRedisRepository.save(new VoiceRedis(roomId, voiceRequestDto.getMemberName(), voiceRequestDto.getContent()));
    }
}
