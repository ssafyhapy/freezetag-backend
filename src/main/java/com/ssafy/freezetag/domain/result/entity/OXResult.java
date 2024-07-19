package com.ssafy.freezetag.domain.result.entity;

import com.ssafy.freezetag.domain.common.BaseEntity;
import com.ssafy.freezetag.domain.room.entity.MemberRoom;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
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
    private String oxResultAnswer;


}
