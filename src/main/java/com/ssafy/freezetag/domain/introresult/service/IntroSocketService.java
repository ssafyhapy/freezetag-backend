package com.ssafy.freezetag.domain.introresult.service;

import com.ssafy.freezetag.domain.exception.custom.MemberNotFoundException;
import com.ssafy.freezetag.domain.exception.custom.RoomNotFoundException;
import com.ssafy.freezetag.domain.introresult.entity.IntroRedis;
import com.ssafy.freezetag.domain.introresult.entity.IntroResult;
import com.ssafy.freezetag.domain.introresult.repository.IntroRedisRepository;
import com.ssafy.freezetag.domain.introresult.repository.IntroResultRepository;
import com.ssafy.freezetag.domain.introresult.service.request.IntroSocketRequestDto;
import com.ssafy.freezetag.domain.introresult.service.response.IntroSocketResponseDto;
import com.ssafy.freezetag.domain.member.entity.Member;
import com.ssafy.freezetag.domain.member.repository.MemberRepository;
import com.ssafy.freezetag.domain.room.entity.MemberRoom;
import com.ssafy.freezetag.domain.room.entity.Room;
import com.ssafy.freezetag.domain.room.repository.MemberRoomRepository;
import com.ssafy.freezetag.domain.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class IntroSocketService {

    private final MemberRepository memberRepository;
    private final MemberRoomRepository memberRoomRepository;
    private final RoomRepository roomRepository;
    private final IntroRedisRepository introRedisRepository;
    private final IntroResultRepository introResultRepository;

    @Transactional
    public void saveIntro(Long roomId, IntroSocketRequestDto introSocketRequestDto) {

        // 자기소개 저장
        MemberRoom memberRoom = memberRoomRepository.findByMemberIdAndRoomId(introSocketRequestDto.getMemberId(), roomId)
                .orElseThrow(() -> new MemberNotFoundException("해당 회원이 현재 방에 참여중이지 않습니다."));

        introRedisRepository.save(new IntroRedis(roomId,
                introSocketRequestDto.getMemberId(),
                memberRoom.getId(),
                introSocketRequestDto.getContent()));
    }

    // 모든 사용자 자기소개 작성 확인
    public boolean checkAllIntro(Long roomId){
        Room room = roomRepository.findById(roomId)
                .orElseThrow(RoomNotFoundException::new);

        List<IntroRedis> intros = introRedisRepository.findAllByRoomId(roomId);

        return intros.size() >= room.getRoomPersonCount();
    }

    @Transactional
    // 다음 자기소개 가져오기 및 DBMS에 저장 그리고 삭제
    public IntroSocketResponseDto getNextIntro(Long roomId){
        List<IntroRedis> intros = introRedisRepository.findAllByRoomId(roomId);

        if(!intros.isEmpty()){
            IntroRedis introRedis = intros.get(0);

            Member member = memberRepository.findById(introRedis.getMemberId())
                    .orElseThrow(() -> new MemberNotFoundException("회원을 찾을 수 없습니다."));

            MemberRoom memberRoom = memberRoomRepository.findById(introRedis.getMemberRoomId())
                    .orElseThrow(() -> new RuntimeException("MemberRoom id가 없습니다"));

            // Redis에 저장된 데이터 삭제 및 DBMS에 저장
            introResultRepository.save(new IntroResult(memberRoom, introRedis.getContent()));
            introRedisRepository.delete(introRedis);

            return new IntroSocketResponseDto(introRedis.getMemberId(),
                    member.getMemberName(),
                    introRedis.getContent());
        }
        throw new RuntimeException("더이상 확인 가능한 자기소개가 남아있지 않습니다.");
    }
}
