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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OXResultService {

    private final OXRedisRepository oxRedisRepository;
    private final OXResultRepository oxResultRepository;
    private final MemberRoomRepository memberRoomRepository;

    public List<OXResponseDto> save(List<OXSaveRequestDto> oxSaveRequestDtoList) {
        List<OXResponseDto> OXResponseDtoList = oxSaveRequestDtoList.stream()
                .map(oxSaveRequestDto -> {
                    OXRedis oxRedis = new OXRedis(
                            oxSaveRequestDto.getRoomId(),
                            oxSaveRequestDto.getMemberRoomId(),
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

    public OXRedis findById(String id){
        return oxRedisRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 OX입니다."));
    }

    public List<OXResponseDto> modify(List<OXModifyRequestDto> oxModifyRequestDtoList){
        List<OXResponseDto> OXResponseDtoList = oxModifyRequestDtoList.stream()
                .map(oxModifyRequestDto -> {
                    OXRedis findOXRedis = findById(oxModifyRequestDto.getId());
                    findOXRedis.update(oxModifyRequestDto.getContent(), oxModifyRequestDto.isAnswer());
                    OXRedis oxRedis = oxRedisRepository.save(findOXRedis);
                    return new OXResponseDto(oxRedis.getId(),
                            oxRedis.getContent(),
                            oxRedis.getAnswer());
                }).toList();

        return OXResponseDtoList;
    }

    public List<OXResponseDto> findAllByRoomId(Long roomId){
        List<OXRedis> oxRedisList = oxRedisRepository.findAllByRoomId(roomId);

        return oxRedisList.stream()
                .map(oxRedis -> new OXResponseDto(oxRedis.getId(),
                        oxRedis.getContent(),
                        oxRedis.getAnswer()))
                .toList();
    }

    @Transactional
    public void deleteAll(Long roomId){
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
