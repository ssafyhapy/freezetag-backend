package com.ssafy.freezetag.domain.room.service;

import com.ssafy.freezetag.domain.member.entity.Member;
import com.ssafy.freezetag.domain.member.repository.MemberRepository;
import com.ssafy.freezetag.domain.room.entity.MemberRoom;
import com.ssafy.freezetag.domain.room.entity.Room;
import com.ssafy.freezetag.domain.room.repository.RoomRepository;
import com.ssafy.freezetag.domain.room.service.request.RoomCreateRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RoomService {
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;

    public void createRoom(RoomCreateRequestDto dto, Long memberId) {
        // DTO 값들로만 일단 Room 엔티티 생성
        // Room 엔티티는 중간 테이블 정보만을 가질 수 있기 때문
        Room unfinishedRoom = new Room(
                dto.getRoomName(),
                dto.getRoomCode(),
                dto.getRoomPersonCount()
        );
        log.info("createRoom parameter : memberId = {}",memberId);
        log.info("createRoom parameter : dto = {}, {}",dto.getRoomName(),dto.getRoomCode());
        // TODO: 찾을 수 없는 memberId 일 경우 사용자 정의 NotFound Exception 처리
        Member member = memberRepository.findById(memberId).orElseThrow();
        log.info("createRoom parameter : findMember = {}", member.getId());
        // 방장 설정
        MemberRoom host = new MemberRoom(member, unfinishedRoom);
        unfinishedRoom.assignHost(host);

        // 완성된 방 정보 저장
        roomRepository.save(unfinishedRoom);
    }
}
