package com.ssafy.freezetag.domain.oxresult.service;

import com.ssafy.freezetag.domain.oxresult.entity.OXResult;
import com.ssafy.freezetag.domain.oxresult.entity.OXRedis;
import com.ssafy.freezetag.domain.oxresult.repository.OXRedisRepository;
import com.ssafy.freezetag.domain.oxresult.repository.OXResultRepository;
import com.ssafy.freezetag.domain.oxresult.service.request.OXModifyRequestDto;
import com.ssafy.freezetag.domain.oxresult.service.request.OXSaveRequestDto;
import com.ssafy.freezetag.domain.oxresult.service.response.OXResponseDto;
import com.ssafy.freezetag.domain.room.entity.MemberRoom;
import com.ssafy.freezetag.domain.room.repository.MemberRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OXResultService {

    private final OXRedisRepository oxRedisRepository;
    private final OXResultRepository oxResultRepository;
    private final MemberRoomRepository memberRoomRepository;

    public List<OXResponseDto> save(Long memberId, List<OXSaveRequestDto> oxSaveRequestDtoList) {
        MemberRoom memberRoom = memberRoomRepository.findByMemberIdAndRoomId(memberId, oxSaveRequestDtoList.get(0).getRoomId())
                .orElseThrow(() -> new RuntimeException("현재 회원이 해당 방에 접속한 상태가 아닙니다."));

        List<OXResponseDto> OXResponseDtoList = oxSaveRequestDtoList.stream()
                .map(oxSaveRequestDto -> {
                    OXRedis oxRedis = new OXRedis(
                            oxSaveRequestDto.getRoomId(),
                            memberId,
                            memberRoom.getId(),
                            oxSaveRequestDto.getContent(),
                            oxSaveRequestDto.isAnswer()
                    );
                    oxRedisRepository.save(oxRedis);
                    return new OXResponseDto(oxRedis.getId(),
                            oxRedis.getContent(),
                            oxRedis.getAnswer());
                }).toList();

        return OXResponseDtoList;
    }

    private List<OXRedis> findOxRedisByMemberIdAndRoomId(Long memberId, Long roomId) {
        MemberRoom memberRoom = memberRoomRepository.findByMemberIdAndRoomId(memberId, roomId)
                .orElseThrow(() -> new IllegalArgumentException("해당 memberRoom이 존재하지 않습니다."));

        return oxRedisRepository.findByMemberRoomId(memberRoom.getId())
                .orElseThrow(() -> new RuntimeException("해당 방에서 회원이 작성한 OX 질문 리스트가 없습니다."));
    }

    public List<OXResponseDto> getOx(Long memberId, Long roomId) {
        List<OXRedis> oxRedisList = findOxRedisByMemberIdAndRoomId(memberId, roomId);

        return oxRedisList.stream()
                .map(oxRedis -> new OXResponseDto(oxRedis.getId(),
                        oxRedis.getContent(),
                        oxRedis.getAnswer()))
                .toList();
    }

    public List<List<OXResponseDto>> getOXs(Long roomId) {
        List<OXRedis> oxRedisList = oxRedisRepository.findAllByRoomId(roomId);

        Map<Long, List<OXRedis>> groupedByMemberRoomId = oxRedisList.stream()
                .collect(Collectors.groupingBy(OXRedis::getMemberRoomId));

        List<List<OXResponseDto>> response = new ArrayList<>();
        for (List<OXRedis> oxGroup : groupedByMemberRoomId.values()) {
            List<OXResponseDto> dtoList = oxGroup.stream()
                    .map(ox -> new OXResponseDto(ox.getId(), ox.getContent(), ox.getAnswer()))
                    .collect(Collectors.toList());
            response.add(dtoList);
        }

        return response;
    }

    public List<OXResponseDto> modify(Long memberId, Long roomId, List<OXModifyRequestDto> oxModifyRequestDtoList) {
        List<OXRedis> oxRedisList = findOxRedisByMemberIdAndRoomId(memberId, roomId);

        for (int i = 0; i < oxRedisList.size(); i++) {
            OXRedis oxRedis = oxRedisList.get(i);
            OXModifyRequestDto modifyRequest = oxModifyRequestDtoList.get(i);
            oxRedis.update(modifyRequest.getContent(), modifyRequest.isAnswer());
        }

        oxRedisRepository.saveAll(oxRedisList);

        return oxRedisList.stream()
                .map(ox -> new OXResponseDto(ox.getId(), ox.getContent(), ox.getAnswer()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteAll(Long roomId) {
        // DB 저장하기
        List<OXRedis> oxRedisList = oxRedisRepository.findAllByRoomId(roomId);

        List<OXResult> oxResultList = oxRedisList.stream()
                .map(oxRedis -> {
                    MemberRoom memberRoom = memberRoomRepository.findById(oxRedis.getMemberRoomId())
                            .orElseThrow(() -> new IllegalArgumentException("해당 memberRoomId가 존재하지 않습니다."));
                    return new OXResult(memberRoom, oxRedis.getContent(), oxRedis.getAnswer());
                }).toList();

        oxResultRepository.saveAll(oxResultList);

        // Redis에서 데이터 삭제하기
        oxRedisRepository.deleteAll(oxRedisList);
    }


}
