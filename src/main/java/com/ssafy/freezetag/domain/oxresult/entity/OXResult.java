package com.ssafy.freezetag.domain.oxresult.entity;

import com.ssafy.freezetag.domain.common.BaseEntity;
import com.ssafy.freezetag.domain.room.entity.MemberRoom;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ox_result")
public class OXResult extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "ox_result_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_room_id")
    private MemberRoom memberRoom;

    @Column(name = "ox_result_content")
    private String oxResultContent;

    @Column(name = "ox_result_answer")
    private Boolean oxResultAnswer;

    public OXResult(MemberRoom memberRoom, String oxResultContent, Boolean oxResultAnswer) {
        this.memberRoom = memberRoom;
        this.oxResultContent = oxResultContent;
        this.oxResultAnswer = oxResultAnswer;
    }
}
