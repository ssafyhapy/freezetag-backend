package com.ssafy.freezetag.domain.member.entity;

import com.ssafy.freezetag.domain.common.BaseEntity;
import com.ssafy.freezetag.domain.room.entity.MemberRoom;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
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
    private List<MemberHistory> memberHistories = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<MemberHobby> memberHobbies = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<MemberInterest> memberInterests = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<MemberRoom> memberRooms = new ArrayList<>();
}
