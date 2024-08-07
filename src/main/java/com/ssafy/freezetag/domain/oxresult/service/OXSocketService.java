package com.ssafy.freezetag.domain.oxresult.service;

import com.ssafy.freezetag.domain.exception.custom.MemberNotFoundException;
import com.ssafy.freezetag.domain.exception.custom.RoomNotFoundException;
import com.ssafy.freezetag.domain.member.entity.Member;
import com.ssafy.freezetag.domain.member.repository.MemberRepository;
import com.ssafy.freezetag.domain.oxresult.entity.OXRedis;
import com.ssafy.freezetag.domain.oxresult.entity.OXResult;
import com.ssafy.freezetag.domain.oxresult.repository.OXRedisRepository;
import com.ssafy.freezetag.domain.oxresult.repository.OXResultRepository;
import com.ssafy.freezetag.domain.oxresult.service.request.OXSocketRequestDto;
import com.ssafy.freezetag.domain.oxresult.service.response.OXSocketResponseDto;
import com.ssafy.freezetag.domain.room.entity.MemberRoom;
import com.ssafy.freezetag.domain.room.entity.Room;
import com.ssafy.freezetag.domain.room.repository.MemberRoomRepository;
import com.ssafy.freezetag.domain.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OXSocketService {

    private final MemberRepository memberRepository;
    private final MemberRoomRepository memberRoomRepository;
    private final RoomRepository roomRepository;
    private final OXRedisRepository oxRedisRepository;
    private final OXResultRepository oxResultRepository;


    @Transactional
    public void saveOX(Long roomId, List<OXSocketRequestDto> oxSocketRequestDtos) {
        // OX 저장
        oxSocketRequestDtos.forEach(oxSocketRequestDto -> {
            MemberRoom memberRoom = memberRoomRepository.findByMemberIdAndRoomId(oxSocketRequestDto.getMemberId(), roomId)
                    .orElseThrow(() -> new MemberNotFoundException("해당 회원이 현재 방에 참여중이지 않습니다."));

            OXRedis oxRedis = new OXRedis(roomId,
                    oxSocketRequestDto.getMemberId(),
                    memberRoom.getId(),
                    oxSocketRequestDto.getContent(),
                    oxSocketRequestDto.isAnswer());
            oxRedisRepository.save(oxRedis);
        });
    }

    public boolean checkAllOX(Long roomId){
        Room room = roomRepository.findById(roomId)
                .orElseThrow(RoomNotFoundException::new);

        List<OXRedis> oxRedisList = oxRedisRepository.findAllByRoomId(roomId);

        // 멤버별로 3개의 OX를 가지고 있어 memberId로 그룹핑한 것의 개수가 0인지 확인
        Map<Long, List<OXRedis>> groupedByMemberId = oxRedisList.stream()
                .collect(Collectors.groupingBy(OXRedis::getMemberId));

        return groupedByMemberId.size() >= room.getRoomPersonCount();
    }

    @Transactional
    public List<OXSocketResponseDto> getNextOX(Long roomId){
        List<OXRedis> oxs = oxRedisRepository.findAllByRoomId(roomId);

        if (oxs.isEmpty()) {
            throw new RuntimeException("더이상 확인 가능한 OX가 남아있지 않습니다.");
        }

        Map<Long, List<OXRedis>> groupedByMemberId = oxs.stream()
                .collect(Collectors.groupingBy(OXRedis::getMemberId));

        List<OXRedis> firstOXRedisList = groupedByMemberId.values().stream()
                .findFirst() // 첫 번째 List<OXRedis>를 가져옵니다.
                .orElseThrow(() -> new RuntimeException("더이상 확인 가능한 OX가 남아있지 않습니다."));


        List<OXSocketResponseDto> oxSocketResponseDtos = firstOXRedisList.stream()
                .map(firstOXRedis -> {
                    Member member = memberRepository.findById(firstOXRedis.getMemberId())
                            .orElseThrow(() -> new MemberNotFoundException("회원을 찾을 수 없습니다."));

                    return new OXSocketResponseDto(firstOXRedis.getMemberId(),
                            member.getMemberName(),
                            firstOXRedis.getContent(),
                            firstOXRedis.getAnswer());
                }).toList();

        List<OXResult> oxResults = firstOXRedisList.stream()
                .map(firstOXRedis -> {
                    MemberRoom memberRoom = memberRoomRepository.findById(firstOXRedis.getMemberRoomId())
                            .orElseThrow(() -> new RuntimeException("MemberRoom id가 없습니다"));

                    return new OXResult(memberRoom,
                            firstOXRedis.getContent(),
                            firstOXRedis.getAnswer());
                }).toList();

        oxResultRepository.saveAll(oxResults);
        oxRedisRepository.deleteAll(firstOXRedisList);

        return oxSocketResponseDtos;
    }
}
