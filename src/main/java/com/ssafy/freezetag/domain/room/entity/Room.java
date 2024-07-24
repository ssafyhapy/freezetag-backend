package com.ssafy.freezetag.domain.room.entity;

import com.ssafy.freezetag.domain.common.BaseEntity;
import com.ssafy.freezetag.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
public class Room extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "room_id")
    private Long id;

    // TODO : 이거 관계 맞는지 확인하기
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberRoom host;

    private String roomName;

    private String roomCode;

    private Integer roomPersonCount;

    @OneToMany(mappedBy = "room")
    private List<MemberRoom> memberRooms;
}
