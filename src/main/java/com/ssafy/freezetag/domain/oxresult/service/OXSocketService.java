package com.ssafy.freezetag.domain.oxresult.service;

import com.ssafy.freezetag.domain.exception.custom.RoomNotFoundException;
import com.ssafy.freezetag.domain.member.entity.Member;
import com.ssafy.freezetag.domain.member.repository.MemberRepository;
import com.ssafy.freezetag.domain.member.service.request.MemberStateSocketRequestDto;
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
import org.springframework.messaging.simp.SimpMessageSendingOperations;
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
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    @Transactional
    public void saveOX(Long roomId, List<OXSocketRequestDto> oxSocketRequestDtos) {
        Long memberId = oxSocketRequestDtos.get(0).getMemberId();

        MemberRoom memberRoom = memberRoomRepository.findByMemberId(memberId)
                .orElseThrow(() -> new RuntimeException("회원이 방에 참여중이지 않습니다."));

        oxSocketRequestDtos.forEach(oxSocketRequestDto -> {
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

        Map<Long, List<OXRedis>> groupedByMemberId = oxRedisList.stream()
                .collect(Collectors.groupingBy(OXRedis::getMemberId));

        return groupedByMemberId.size() >= room.getRoomPersonCount();
    }

    @Transactional
    public List<OXSocketResponseDto> getNextOX(Long roomId){
        List<OXRedis> oxs = oxRedisRepository.findAllByRoomId(roomId);

        if (oxs.isEmpty()) {
            simpMessageSendingOperations.convertAndSend("/api/sub/" + roomId + "/state",
                    new MemberStateSocketRequestDto("balance"));
        }

        Map<Long, List<OXRedis>> groupedByMemberId = oxs.stream()
                .collect(Collectors.groupingBy(OXRedis::getMemberId));

        List<OXRedis> firstOXRedisList = groupedByMemberId.values().stream()
                .findFirst() // 첫 번째 List<OXRedis>를 가져옵니다.
                .orElseThrow(() -> new RuntimeException("더이상 확인 가능한 OX가 남아있지 않습니다."));

        Member member = memberRepository.findById(firstOXRedisList.get(0).getMemberId())
                .orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));

        List<OXSocketResponseDto> oxSocketResponseDtos = firstOXRedisList.stream()
                .map(firstOXRedis -> new OXSocketResponseDto(firstOXRedis.getMemberId(),
                        member.getMemberName(),
                        firstOXRedis.getContent(),
                        firstOXRedis.getAnswer()))
                .toList();

        MemberRoom memberRoom = memberRoomRepository.findById(firstOXRedisList.get(0).getMemberRoomId())
                .orElseThrow(() -> new RuntimeException("MemberRoom id가 없습니다"));

        List<OXResult> oxResults = firstOXRedisList.stream()
                .map(firstOXRedis -> new OXResult(memberRoom,
                        firstOXRedis.getContent(),
                        firstOXRedis.getAnswer()))
                .toList();

        oxResultRepository.saveAll(oxResults);
        oxRedisRepository.deleteAll(firstOXRedisList);

        return oxSocketResponseDtos;
    }
}
