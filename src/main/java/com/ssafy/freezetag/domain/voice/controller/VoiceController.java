package com.ssafy.freezetag.domain.voice.controller;

import com.ssafy.freezetag.domain.member.entity.Member;
import com.ssafy.freezetag.domain.member.service.MemberService;
import com.ssafy.freezetag.domain.voice.entity.VoiceRedis;
import com.ssafy.freezetag.domain.voice.service.VoiceService;
import com.ssafy.freezetag.domain.voice.service.request.VoiceRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class VoiceController {

    private final VoiceService voiceService;
    private final MemberService memberService;
    /**
     * "/api/pub/voice/{roomId}" 경로로 클라이언트가 STT된 텍스트를 보내면 redis에 저장
     */
    @MessageMapping("/voice/{roomId}")
    public void voice(@DestinationVariable Long roomId, VoiceRequestDto voiceRequestDto) {
        VoiceRedis voiceRedis = voiceService.saveVoice(roomId, voiceRequestDto);
        Member member = memberService.findMember(voiceRequestDto.getMemberId());

        log.info("{}번 방에 {}님이 {}를 밀히였습니다.", roomId, voiceRequestDto.getMemberName(), voiceRequestDto.getContent());

    }
}
