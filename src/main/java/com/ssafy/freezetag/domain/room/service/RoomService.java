package com.ssafy.freezetag.domain.room.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssafy.freezetag.domain.exception.custom.MemberNotInRoomException;
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
import com.ssafy.freezetag.domain.room.service.response.RoomMemberInfoResponseDto;
import com.ssafy.freezetag.domain.room.service.response.RoomUserJoinEvent;
import com.ssafy.freezetag.global.util.CodeGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ssafy.freezetag.domain.room.service.helper.RoomConverter.convertToMemberInfoDtos;
import static com.ssafy.freezetag.domain.room.service.helper.RoomConverter.createRoomConnectResponseDto;
import static com.ssafy.freezetag.domain.room.service.helper.RoomValidator.validateDuplicateMember;
import static com.ssafy.freezetag.domain.room.service.helper.RoomValidator.validateRoomCapacity;

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

        // 완성된 방 정보 저장
        roomRepository.save(unfinishedRoom);

        // room Id로 연관된 멤버들 다 조인해오는 메소드
        // 현재 엔티티가 LAZY로 설정되어 있어서 이렇게 페치 조인해오지 않으면 쿼리가 여러번 나간다.
        Room room = fetchRoomWithMembers(unfinishedRoom.getId());

        // RoomMemberInfoResponseDto (memberId, memberName) 형태로 변환
        List<RoomMemberInfoResponseDto> memberInfoDtos = convertToMemberInfoDtos(room.getMemberRooms());

        // Openvidu 로부터 토큰 및 세션 ID 반환
        OpenviduResponseDto webrtcDto = openviduService.createRoom();

        // Redis에 세션 및 방 정보 저장
        roomRedisService.saveRoomInfo(enterCode, webrtcDto.getSessionId(), room.getId());

        // Redis에서 채팅(메세지) 정보 불러오기
        List<MessageRedis> messages = messageService.getMessages(room.getId());

        return createRoomConnectResponseDto(room, enterCode, dto.getRoomName(), dto.getRoomPersonCount(), memberInfoDtos, webrtcDto, messages);

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

        // RoomMemberInfoResponseDto (memberId, memberName) 형태로 변환
        List<RoomMemberInfoResponseDto> memberInfoDtos = convertToMemberInfoDtos(fetchJoinedRoom.getMemberRooms());

        // OpenVidu 토큰 반환
        OpenviduResponseDto webrtcDto = openviduService.enterRoom(sessionId);

        // 신규 회원 정보를 event 형태로 저장
        RoomMemberInfoResponseDto newMemberInfo = new RoomMemberInfoResponseDto(memberId, member.getMemberName());
        RoomUserJoinEvent roomUserJoinEvent = new RoomUserJoinEvent("NEW_USER_JOINED", roomId, newMemberInfo);

        // 기존 사용자에게 새로 입장된 유저 알림
        messageService.sendUserJoinEvent(fetchJoinedRoom.getId(), roomUserJoinEvent);

        // Redis에서 채팅(메세지) 정보 불러오기
        List<MessageRedis> messages = messageService.getMessages(roomId);

        // 현재 방 정보 반환
        return createRoomConnectResponseDto(fetchJoinedRoom, enterCode, fetchJoinedRoom.getRoomName(), fetchJoinedRoom.getRoomPersonCount(), memberInfoDtos, webrtcDto, messages);

    }

    @Transactional
    public void exitRoom(Long roomId, Long memberId) {
        log.info("exitRoom(roomId={}, memberId={})", roomId, memberId);

        // 실제 회원이 맞는지 조회
        Member member = memberService.findMember(memberId);

        // 실제 회원이라면 MemberRoom 조회
        MemberRoom memberRoom = getMemberRoom(roomId, memberId);

        // 해당 방 멤버들 모두 조회
        Room room = fetchRoomWithMemberRooms(roomId);

        // 방장인지 확인
        boolean isHost = room.getHost()
                .getId()
                .equals(memberRoom.getId());

        // 방장 랜덤 설정
        if (isHost) {
            room.assignRandomHost();
        }

        // 방 테이블을 조회 후 멤버 룸을 삭제
        room.getMemberRooms().remove(memberRoom);
        memberRoomRepository.deleteByMemberIdAndRoomId(memberId, roomId);

    }

    private MemberRoom getMemberRoom(final Long roomId, final Long memberId) {
        return memberRoomRepository.findByMemberIdAndRoomId(memberId, roomId)
                .orElseThrow(MemberNotInRoomException::new);
    }

    public Room fetchRoomWithMembers(Long roomId) {
        return roomRepository.findWithMembersById(roomId)
                .orElseThrow(RoomNotFoundException::new);
    }

    public Room fetchRoomWithMemberRooms(Long roomId) {
        return roomRepository.findWithMemberRoomsById(roomId)
                .orElseThrow(RoomNotFoundException::new);
    }

}
