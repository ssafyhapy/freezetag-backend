package com.ssafy.freezetag.domain.room.service;

import com.ssafy.freezetag.domain.member.entity.Member;
import com.ssafy.freezetag.domain.member.repository.MemberRepository;
import com.ssafy.freezetag.domain.room.entity.MemberRoom;
import com.ssafy.freezetag.domain.room.entity.Room;
import com.ssafy.freezetag.domain.room.repository.RoomRepository;
import com.ssafy.freezetag.domain.room.service.request.OpenviduResponseDto;
import com.ssafy.freezetag.domain.room.service.request.RoomCreateRequestDto;
import com.ssafy.freezetag.domain.room.service.response.RoomCreateResponseDto;
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

    @Transactional
    public RoomCreateResponseDto createRoom(RoomCreateRequestDto dto, Long memberId) {
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

        return new RoomCreateResponseDto(
                roomId,
                enterCode,
                dto.getRoomName(),
                dto.getRoomPersonCount(),
                memberInfoDtos,
                room.getHost().getId(),
                webrtcDto);
    }

    @Transactional
    public RoomCreateResponseDto enterRoom(String roomCode, Long memberId) {
        // memberId 유효성 검증

        // roomCode로 방 조회

        // 방 인원수 확인 (초과 시 예외 처리)

        // 방 입장 처리 (memberRooms 리스트에 업데이트)

        // OpenVidu 토큰 반환

        // 기존 사용자에게 입장된 유저 띄워주기? (보류)
    }
}
