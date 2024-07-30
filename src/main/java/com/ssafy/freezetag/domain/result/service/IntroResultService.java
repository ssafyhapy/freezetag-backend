package com.ssafy.freezetag.domain.result.service;

import com.ssafy.freezetag.domain.result.entity.IntroResult;
import com.ssafy.freezetag.domain.result.entity.redis.IntroRedis;
import com.ssafy.freezetag.domain.result.repository.IntroRedisRepository;
import com.ssafy.freezetag.domain.result.repository.IntroResultRepository;
import com.ssafy.freezetag.domain.result.service.request.IntroModifyRequestDto;
import com.ssafy.freezetag.domain.result.service.request.IntroSaveRequestDto;
import com.ssafy.freezetag.domain.result.service.response.IntroResponseDto;
import com.ssafy.freezetag.domain.result.service.response.IntroSaveResponseDto;
import com.ssafy.freezetag.domain.room.entity.MemberRoom;
import com.ssafy.freezetag.domain.room.repository.MemberRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class IntroResultService {

    private final IntroRedisRepository introRedisRepository;
    private final IntroResultRepository introResultRepository;
    private final MemberRoomRepository memberRoomRepository;

    public IntroSaveResponseDto save(IntroSaveRequestDto introSaveRequestDto) {
        IntroRedis introRedis = new IntroRedis(introSaveRequestDto.getRoomId(),
                                                introSaveRequestDto.getMemberRoomId(),
                                                introSaveRequestDto.getContent());

        IntroRedis savedIntroRedis = introRedisRepository.save(introRedis);

        return new IntroSaveResponseDto(savedIntroRedis.getId(),
                savedIntroRedis.getRoomId(),
                savedIntroRedis.getMemberRoomId(),
                savedIntroRedis.getContent());
    }

    public IntroRedis findById(String id) {
        return introRedisRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 한 줄 자기소개입니다."));
    }

    public IntroResponseDto modify(IntroModifyRequestDto introModifyRequestDto) {
        IntroRedis findIntroRedis = findById(introModifyRequestDto.getId());

        findIntroRedis.update(introModifyRequestDto.getContent());
        IntroRedis savedIntroRedis = introRedisRepository.save(findIntroRedis);

        return new IntroResponseDto(savedIntroRedis.getId(), savedIntroRedis.getContent());
    }

    public List<IntroResponseDto> findAllByRoomId(Long roomId) {
        List<IntroRedis> introRedisList = introRedisRepository.findAllByRoomId(roomId);

        return introRedisList.stream()
                .map(introRedis -> new IntroResponseDto(introRedis.getId(), introRedis.getContent()))
                .toList();
    }

    @Transactional
    public void deleteAll(Long roomId) {
        // DB 저장하기
        List<IntroRedis> introRedisList = introRedisRepository.findAllByRoomId(roomId);

        List<IntroResult> introResultList = introRedisList.stream()
                .map(introRedis -> {
                    MemberRoom memberRoom = memberRoomRepository.findById(introRedis.getMemberRoomId())
                        .orElseThrow(() -> new IllegalArgumentException("해당 memberRoomId가 존재하지 않습니다. " + introRedis.getMemberRoomId()));

                    return new IntroResult(memberRoom, introRedis.getContent());
        }).toList();

        introResultRepository.saveAll(introResultList);

        // Redis에서 데이터 삭제하기
        introRedisRepository.deleteAll(introRedisList);
    }
}
