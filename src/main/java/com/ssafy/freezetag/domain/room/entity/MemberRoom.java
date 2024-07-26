package com.ssafy.freezetag.domain.room.entity;

import com.ssafy.freezetag.domain.common.BaseEntity;
import com.ssafy.freezetag.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberRoom extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "member_room_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    public MemberRoom(final Member member, final Room room) {
        this.member = member;
        this.room = room;
    }
}
