package com.ssafy.freezetag.domain.member.entity;

import com.ssafy.freezetag.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Generated;
import lombok.Getter;

@Getter
@Entity
public class MemberHobby extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "member_hobby_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String memberHobbyContent;
}
