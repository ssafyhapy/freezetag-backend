package com.ssafy.freezetag.domain.room.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.freezetag.domain.exception.custom.DuplicateRoomMemberException;
import com.ssafy.freezetag.domain.exception.custom.RoomFullException;
import com.ssafy.freezetag.domain.exception.custom.SessionNotFoundException;
import com.ssafy.freezetag.domain.member.entity.Member;
import com.ssafy.freezetag.domain.member.repository.MemberRepository;
import com.ssafy.freezetag.domain.message.entity.MessageRedis;
import com.ssafy.freezetag.domain.message.service.MessageService;
import com.ssafy.freezetag.domain.room.entity.MemberRoom;
import com.ssafy.freezetag.domain.room.entity.Room;
import com.ssafy.freezetag.domain.room.entity.RoomRedis;
import com.ssafy.freezetag.domain.room.repository.MemberRoomRepository;
import com.ssafy.freezetag.domain.room.repository.RoomRedisRepository;
import com.ssafy.freezetag.domain.room.repository.RoomRepository;
import com.ssafy.freezetag.domain.room.service.request.OpenviduResponseDto;
import com.ssafy.freezetag.domain.room.service.request.RoomCreateRequestDto;
import com.ssafy.freezetag.domain.room.service.response.RoomConnectResponseDto;
import com.ssafy.freezetag.domain.room.service.response.RoomMemberInfoDto;
import com.ssafy.freezetag.domain.room.service.response.RoomUserJoinEvent;
import com.ssafy.freezetag.global.util.CodeGenerator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final MemberRoomRepository memberRoomRepository;
    private final MessageService messageService;

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

        memberRoomRepository.save(host);
        unfinishedRoom.assignHost(host);

        // 완성된 방 정보 저장
        roomRepository.save(unfinishedRoom);

        // room Id로 연관된 멤버들 다 조인해오는 메소드
        // 현재 엔티티가 LAZY로 설정되어 있어서 이렇게 페치 조인해오지 않으면 쿼리가 여러번 나간다.
        Room room = roomRepository.findById(unfinishedRoom.getId()).orElseThrow();

        // RoomMemberInfoDto (memberId, memberName) 형태로 변환
        List<RoomMemberInfoDto> memberInfoDtos = room.getMemberRooms().stream()
                .map(memberRoom -> new RoomMemberInfoDto(memberRoom.getId(), memberRoom.getMember().getMemberName()))
                .toList();

        // DB ID 조회
        Long roomId = room.getId();

        // Openvidu 로부터 토큰 및 세션 ID 반환
        OpenviduResponseDto webrtcDto = openviduService.createRoom();

        // Redis에 세션 및 방 정보 저장
        RoomRedis roomRedis = new RoomRedis(enterCode, webrtcDto.getSessionId(), roomId);
        roomRedisRepository.save(roomRedis);

        // Redis에서 채팅(메세지) 정보 불러오기
        List<MessageRedis> messages = messageService.getMessages(roomId);


        return new RoomConnectResponseDto(
                roomId,
                enterCode,
                dto.getRoomName(),
                dto.getRoomPersonCount(),
                memberInfoDtos,
                room.getHost().getId(),
                webrtcDto,
                messages);
    }

    @Transactional
    public RoomConnectResponseDto enterRoom(String enterCode, Long memberId) throws JsonProcessingException {
        log.info("enterRoom(enterCode={}, memberId={})", enterCode, memberId);
        // memberId 유효성 검증
        Member member = memberRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);

        // 레디스에서 roomCode로 openvidu 세션, 방 ID 조회
        RoomRedis roomRedis = roomRedisRepository.findById(enterCode).orElseThrow(() -> new SessionNotFoundException(enterCode));
        String sessionId = roomRedis.getSessionId();
        Long roomId = roomRedis.getRoomId();
        log.info("roomRedisRepository 조회 = [{}, {}]", sessionId, roomId);

        // 방 인원수 확인 (초과 시 예외 처리)
        Room fetchJoinedRoom = roomRepository.findById(roomId).orElseThrow();
        if (fetchJoinedRoom.getMemberRooms().size() == fetchJoinedRoom.getRoomPersonCount()) {
            throw new RoomFullException();
        }
        
        // 이미 입장된 회원이 접속을 시도한 경우 처리
        List<MemberRoom> existMemberRooms = fetchJoinedRoom.getMemberRooms();
        if (existMemberRooms.stream()
                .anyMatch(memberRoom -> memberRoom.getMember().getId() == memberId)) {
            throw new DuplicateRoomMemberException();
        }

        // 방 입장 처리 (memberRooms 리스트에 업데이트)
        MemberRoom nowMemberRoom = new MemberRoom(member, fetchJoinedRoom);
        memberRoomRepository.save(nowMemberRoom);
        fetchJoinedRoom.getMemberRooms().add(nowMemberRoom);

        // RoomMemberInfoDto (memberId, memberName) 형태로 변환
        List<RoomMemberInfoDto> memberInfoDtos = fetchJoinedRoom.getMemberRooms().stream()
                .map(memberRoom -> new RoomMemberInfoDto(memberRoom.getMember().getId(), memberRoom.getMember().getMemberName()))
                .toList();

        // OpenVidu 토큰 반환
        OpenviduResponseDto webrtcDto = openviduService.enterRoom(sessionId);

        // 신규 회원 정보를 event 형태로 저장
        RoomMemberInfoDto newMemberInfo = new RoomMemberInfoDto(memberId, member.getMemberName());
        RoomUserJoinEvent roomUserJoinEvent = new RoomUserJoinEvent("NEW_USER_JOINED", roomId, newMemberInfo);

        // 기존 사용자에게 새로 입장된 유저 알림
        String joinEvent = objectMapper.writeValueAsString(roomUserJoinEvent);
        messagingTemplate.convertAndSend("/sub/room/" + roomId, joinEvent);

        // Redis에서 채팅(메세지) 정보 불러오기
        List<MessageRedis> messages = messageService.getMessages(roomId);

        // 현재 방 정보 반환
        return new RoomConnectResponseDto(
                roomId,
                enterCode,
                fetchJoinedRoom.getRoomName(),
                fetchJoinedRoom.getRoomPersonCount(),
                memberInfoDtos,
                fetchJoinedRoom.getHost().getId(),
                webrtcDto,
                messages);
    }

}
