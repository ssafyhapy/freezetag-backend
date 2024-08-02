package com.ssafy.freezetag.domain.introresult.service;

import com.ssafy.freezetag.domain.introresult.entity.IntroResult;
import com.ssafy.freezetag.domain.introresult.entity.IntroRedis;
import com.ssafy.freezetag.domain.introresult.repository.IntroRedisRepository;
import com.ssafy.freezetag.domain.introresult.repository.IntroResultRepository;
import com.ssafy.freezetag.domain.introresult.service.request.IntroModifyRequestDto;
import com.ssafy.freezetag.domain.introresult.service.request.IntroSaveRequestDto;
import com.ssafy.freezetag.domain.introresult.service.response.IntroResponseDto;
import com.ssafy.freezetag.domain.introresult.service.response.IntroSaveResponseDto;
import com.ssafy.freezetag.domain.room.entity.MemberRoom;
import com.ssafy.freezetag.domain.room.repository.MemberRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class IntroResultService {

    private final IntroRedisRepository introRedisRepository;
    private final IntroResultRepository introResultRepository;
    private final MemberRoomRepository memberRoomRepository;

    public IntroSaveResponseDto save(Long memberId, IntroSaveRequestDto introSaveRequestDto) {
        MemberRoom memberRoom = memberRoomRepository.findByMemberIdAndRoomId(memberId, introSaveRequestDto.getRoomId())
                .orElseThrow(() -> new RuntimeException("현재 회원이 해당 방에 접속한 상태가 아닙니다."));

        IntroRedis introRedis = new IntroRedis(introSaveRequestDto.getRoomId(),
                                                memberId,
                                                memberRoom.getId(),
                                                introSaveRequestDto.getContent());

        IntroRedis savedIntroRedis = introRedisRepository.save(introRedis);

        return new IntroSaveResponseDto(savedIntroRedis.getId(),
                savedIntroRedis.getRoomId(),
                savedIntroRedis.getMemberId(),
                memberRoom.getId(),
                savedIntroRedis.getContent());
    }

    private IntroRedis findIntroRedisByMemberIdAndRoomId(Long memberId, Long roomId){
        MemberRoom memberRoom = memberRoomRepository.findByMemberIdAndRoomId(memberId, roomId)
                .orElseThrow(() -> new IllegalArgumentException("해당 memberRoom이 존재하지 않습니다."));

        return introRedisRepository.findByMemberRoomId(memberRoom.getId())
                .orElseThrow(() -> new RuntimeException("해당 방에서 회원이 작성한 한 줄 자기소개가 없습니다."));
    }

    public IntroResponseDto getMyIntro(Long memberId, Long roomId){
        IntroRedis findIntroRedis = findIntroRedisByMemberIdAndRoomId(memberId, roomId);

        return new IntroResponseDto(findIntroRedis.getId(), findIntroRedis.getMemberId(), findIntroRedis.getContent());
    }

    @Transactional
    public IntroResponseDto modify(Long memberId, Long roomId, IntroModifyRequestDto introModifyRequestDto) {
        IntroRedis findIntroRedis = findIntroRedisByMemberIdAndRoomId(memberId, roomId);

        findIntroRedis.update(introModifyRequestDto.getContent());
        IntroRedis savedIntroRedis = introRedisRepository.save(findIntroRedis);

        return new IntroResponseDto(savedIntroRedis.getId(), savedIntroRedis.getMemberId(), savedIntroRedis.getContent());
    }

    public List<IntroResponseDto> findAllByRoomId(Long roomId) {
        List<IntroRedis> introRedisList = introRedisRepository.findAllByRoomId(roomId);

        return introRedisList.stream()
                .map(introRedis -> new IntroResponseDto(introRedis.getId(), introRedis.getMemberId(), introRedis.getContent()))
                .toList();
    }

    @Transactional
    public void deleteAll(Long roomId) {
        // DB 저장하기
        List<IntroRedis> introRedisList = introRedisRepository.findAllByRoomId(roomId);

        List<IntroResult> introResultList = introRedisList.stream()
                .map(introRedis -> {
                    MemberRoom memberRoom = memberRoomRepository.findByMemberIdAndRoomId(introRedis.getMemberId(), introRedis.getRoomId())
                        .orElseThrow(() -> new IllegalArgumentException("해당 memberRoom이 존재하지 않습니다. "));

                    return new IntroResult(memberRoom, introRedis.getContent());
        }).toList();

        introResultRepository.saveAll(introResultList);

        // Redis에서 데이터 삭제하기
        introRedisRepository.deleteAll(introRedisList);
    }
}
