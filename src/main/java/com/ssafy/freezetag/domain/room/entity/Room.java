package com.ssafy.freezetag.domain.room.entity;

import com.ssafy.freezetag.domain.common.BaseEntity;
import com.ssafy.freezetag.domain.result.entity.BalanceQuestion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Room extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "room_id")
    private Long id;

    // TODO : 이거 관계 맞는지 확인하기
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_room_id")
    private MemberRoom host;

    private String roomName;

    private String roomCode;

    private Integer roomPersonCount;

    @OneToMany(mappedBy = "room")
    private List<MemberRoom> memberRooms = new ArrayList<>();

    @OneToMany(mappedBy = "room")
    private List<BalanceQuestion> balanceQuestions = new ArrayList<>();

    public Room(final String roomName, final String roomCode, final Integer roomPersonCount) {
        this.roomName = roomName;
        this.roomCode = roomCode;
        this.roomPersonCount = roomPersonCount;
    }

    // Room 생성 후 의존관계 주입
    public void assignHost(final MemberRoom host) {
        this.host = host;
        memberRooms.add(host);

        // 양방향 관계 설정
        host.setRoom(this);
    }




}
