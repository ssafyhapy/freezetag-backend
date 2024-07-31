package com.ssafy.freezetag.domain.room.service;

import com.ssafy.freezetag.domain.exception.custom.RoomFullException;
import com.ssafy.freezetag.domain.exception.custom.SessionNotFoundException;
import com.ssafy.freezetag.domain.member.entity.Member;
import com.ssafy.freezetag.domain.member.repository.MemberRepository;
import com.ssafy.freezetag.domain.room.entity.MemberRoom;
import com.ssafy.freezetag.domain.room.entity.Room;
import com.ssafy.freezetag.domain.room.entity.RoomRedis;
import com.ssafy.freezetag.domain.room.repository.RoomRedisRepository;
import com.ssafy.freezetag.domain.room.repository.RoomRepository;
import com.ssafy.freezetag.domain.room.service.request.OpenviduResponseDto;
import com.ssafy.freezetag.domain.room.service.request.RoomCreateRequestDto;
import com.ssafy.freezetag.domain.room.service.response.RoomConnectResponseDto;
import com.ssafy.freezetag.domain.room.service.response.RoomMemberInfoDto;
import com.ssafy.freezetag.global.util.CodeGenerator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RoomService {
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final OpenviduService openviduService;
    private final RoomRedisRepository roomRedisRepository;

    @Transactional
    public RoomConnectResponseDto createRoom(RoomCreateRequestDto dto, Long memberId) {
        // 접속코드 생성
        String enterCode = CodeGenerator.generateCode();

        // DTO 값들로만 일단 Room 엔티티 생성
        // Room 엔티티는 중간 테이블 정보만을 가질 수 있기 때문
        Room unfinishedRoom = new Room(
                dto.getRoomName(),
                enterCode,
                dto.getRoomPersonCount()
        );

        // 실제 회원이 있는지 확인
        Member member = memberRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);

        // 방장 설정
        MemberRoom host = new MemberRoom(member, unfinishedRoom);
        unfinishedRoom.assignHost(host);

        // 완성된 방 정보 저장
        roomRepository.save(unfinishedRoom);

        // room Id로 연관된 멤버들 다 조인해오는 메소드
        // 현재 엔티티가 LAZY로 설정되어 있어서 이렇게 페치 조인해오지 않으면 쿼리가 여러번 나간다.
        Room room = roomRepository.findRoomWithMembers(unfinishedRoom.getId());

        // RoomMemberInfoDto (memberId, memberName) 형태로 변환
        List<RoomMemberInfoDto> memberInfoDtos = room.getMemberRooms().stream()
                .map(memberRoom -> new RoomMemberInfoDto(memberRoom.getId(), memberRoom.getMember().getMemberName()))
                .toList();

        // DB ID 조회
        Long roomId = room.getId();

        // Openvidu 로부터 토큰 및 세션 ID 반환
        OpenviduResponseDto webrtcDto = openviduService.createRoom();

        return new RoomConnectResponseDto(
                roomId,
                enterCode,
                dto.getRoomName(),
                dto.getRoomPersonCount(),
                memberInfoDtos,
                room.getHost().getId(),
                webrtcDto);
    }

    @Transactional
    public RoomConnectResponseDto enterRoom(String enterCode, Long memberId) {
        // memberId 유효성 검증
        Member member = memberRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);

        // 레디스에서 roomCode로 openvidu 세션, 방 ID 조회
        RoomRedis roomRedis = roomRedisRepository.findById(enterCode).orElseThrow(() -> new SessionNotFoundException(enterCode));
        String sessionId = roomRedis.getSessionId();
        Long roomId = roomRedis.getRoomId();

        // 방 인원수 확인 (초과 시 예외 처리)
        Room fetchJoinedRoom = roomRepository.findRoomWithMembers(roomId);
        if (fetchJoinedRoom.getMemberRooms().size() >= 6) {
            throw new RoomFullException();
        }

        // 방 입장 처리 (memberRooms 리스트에 업데이트)
        fetchJoinedRoom.getMemberRooms().add(new MemberRoom(member, fetchJoinedRoom));

        // RoomMemberInfoDto (memberId, memberName) 형태로 변환
        List<RoomMemberInfoDto> memberInfoDtos = fetchJoinedRoom.getMemberRooms().stream()
                .map(memberRoom -> new RoomMemberInfoDto(memberRoom.getId(), memberRoom.getMember().getMemberName()))
                .toList();

        // OpenVidu 토큰 반환
        OpenviduResponseDto webrtcDto = openviduService.enterRoom(sessionId);

        // 기존 사용자에게 입장된 유저 알림 (신규 유저 정보만을 넘겨줌)

        // 현재 방 정보 반환
        return new RoomConnectResponseDto(
                roomId,
                enterCode,
                fetchJoinedRoom.getRoomName(),
                fetchJoinedRoom.getRoomPersonCount(),
                memberInfoDtos,
                fetchJoinedRoom.getHost().getId(),
                webrtcDto);
    }
}
