package com.ssafy.freezetag.domain.result.entity;

import com.ssafy.freezetag.domain.common.BaseEntity;
import com.ssafy.freezetag.domain.room.entity.MemberRoom;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class IntroResult extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "intro_result_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_room_id")
    private MemberRoom memberRoom;

    private String content;
}
