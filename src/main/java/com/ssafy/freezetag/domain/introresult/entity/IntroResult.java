package com.ssafy.freezetag.domain.introresult.entity;

import com.ssafy.freezetag.domain.common.BaseEntity;
import com.ssafy.freezetag.domain.room.entity.MemberRoom;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IntroResult extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "intro_result_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_room_id")
    private MemberRoom memberRoom;

    private String content;

    public IntroResult(MemberRoom memberRoom, String content) {
        this.memberRoom = memberRoom;
        this.content = content;
    }
}
