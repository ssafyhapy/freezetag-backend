package com.ssafy.freezetag.domain.room.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssafy.freezetag.domain.exception.custom.MemberNotFoundException;
import com.ssafy.freezetag.domain.exception.custom.RoomNotFoundException;
import com.ssafy.freezetag.domain.member.entity.Member;
import com.ssafy.freezetag.domain.member.service.MemberService;
import com.ssafy.freezetag.domain.message.entity.MessageRedis;
import com.ssafy.freezetag.domain.message.service.MessageService;
import com.ssafy.freezetag.domain.room.entity.MemberRoom;
import com.ssafy.freezetag.domain.room.entity.Room;
import com.ssafy.freezetag.domain.room.entity.RoomRedis;
import com.ssafy.freezetag.domain.room.repository.MemberRoomRepository;
import com.ssafy.freezetag.domain.room.repository.RoomRepository;
import com.ssafy.freezetag.domain.room.service.request.RoomCreateRequestDto;
import com.ssafy.freezetag.domain.room.service.response.OpenviduResponseDto;
import com.ssafy.freezetag.domain.room.service.response.RoomConnectResponseDto;
import com.ssafy.freezetag.global.util.CodeGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ssafy.freezetag.domain.room.service.helper.RoomConverter.createRoomConnectResponseDto;
import static com.ssafy.freezetag.domain.room.service.helper.RoomValidator.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RoomService {
    private final MemberService memberService;
    private final RoomRedisService roomRedisService;
    private final RoomRepository roomRepository;
    private final OpenviduService openviduService;
    private final MemberRoomRepository memberRoomRepository;
    private final MessageService messageService;

    @Transactional
    public RoomConnectResponseDto createRoom(RoomCreateRequestDto dto, Long memberId) {
        // 6명 초과 방 생성 예외
        validateMaxRoomPersonCount(dto.getRoomPersonCount());

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
        Member member = memberService.findMember(memberId);

        // 방장 설정
        MemberRoom host = new MemberRoom(member, unfinishedRoom);

        memberRoomRepository.save(host);
        unfinishedRoom.assignHost(host);

        // 방장도 멤버 목록에 저장
        unfinishedRoom.getMemberRooms().add(host);

        // 완성된 방 정보 저장
        roomRepository.save(unfinishedRoom);

        // room Id로 연관된 멤버들 다 조인해오는 메소드
        // 현재 엔티티가 LAZY로 설정되어 있어서 이렇게 페치 조인해오지 않으면 쿼리가 여러번 나간다.
        Room room = fetchRoomWithMembers(unfinishedRoom.getId());

        // Openvidu 로부터 토큰 및 세션 ID 반환
        OpenviduResponseDto webrtcDto = openviduService.createRoom(member);

        // Redis에 세션 및 방 정보 저장
        roomRedisService.saveRoomInfo(enterCode, webrtcDto.getSessionId(), room.getId());

        // Redis에서 채팅(메세지) 정보 불러오기
        List<MessageRedis> messages = messageService.getMessages(room.getId());

        return createRoomConnectResponseDto(
                room,
                enterCode,
                dto.getRoomName(),
                dto.getRoomPersonCount(),
                webrtcDto,
                messages
        );

    }

    @Transactional
    public RoomConnectResponseDto enterRoom(String enterCode, Long memberId) throws JsonProcessingException {
        log.info("enterRoom(enterCode={}, memberId={})", enterCode, memberId);

        // memberId 유효성 검증
        Member member = memberService.findMember(memberId);

        // 레디스에서 enterCode로 openvidu 세션, 방 ID 조회
        RoomRedis roomRedis = roomRedisService.fetchRoomInfo(enterCode);

        String sessionId = roomRedis.getSessionId();
        Long roomId = roomRedis.getRoomId();
        log.info("roomRedisRepository 조회 = [{}, {}]", sessionId, roomId);

        // 방 조회 (패치 조인)
        Room fetchJoinedRoom = fetchRoomWithMembers(roomId);

        // 방 인원수 확인 (초과 시 예외 처리)
        validateRoomCapacity(fetchJoinedRoom);

        // 이미 입장된 회원이 접속을 시도한 경우 처리
        validateDuplicateMember(fetchJoinedRoom, memberId);

        // 방 입장 처리 (memberRooms 리스트에 업데이트)
        MemberRoom nowMemberRoom = new MemberRoom(member, fetchJoinedRoom);
        memberRoomRepository.save(nowMemberRoom);
        fetchJoinedRoom.getMemberRooms().add(nowMemberRoom);

        // OpenVidu 토큰 반환
        OpenviduResponseDto webrtcDto = openviduService.enterRoom(sessionId, member);

        // Redis에서 채팅(메세지) 정보 불러오기
        List<MessageRedis> messages = messageService.getMessages(roomId);

        // 현재 방 정보 반환
        return createRoomConnectResponseDto(
                fetchJoinedRoom,
                enterCode,
                fetchJoinedRoom.getRoomName(),
                fetchJoinedRoom.getRoomPersonCount(),
                webrtcDto,
                messages
        );

    }

    public Room fetchRoomWithMembers(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(RoomNotFoundException::new);
    }

}
