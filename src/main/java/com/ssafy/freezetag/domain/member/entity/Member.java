package com.ssafy.freezetag.domain.member.entity;

import com.ssafy.freezetag.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
public class Member extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String memberName;

    private String memberProviderEmail;

    private String memberProvider;

    @Enumerated(EnumType.STRING)
    private Age memberAge;

    private String memberProfileImageUrl;

    private String memberIntroduction;

    @OneToMany(mappedBy = "member")
    private List<MemberHistory> memberHistories;
}
