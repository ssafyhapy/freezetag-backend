package com.ssafy.freezetag.domain.member.entity;

import com.ssafy.freezetag.domain.common.BaseEntity;
import com.ssafy.freezetag.domain.balanceresult.entity.BalanceResult;
import com.ssafy.freezetag.domain.room.entity.MemberRoom;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    private List<MemberRoom> memberRooms = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<BalanceResult> balanceResults = new ArrayList<>();

    @Setter
    @ColumnDefault("true")
    private boolean memberVisibility;

    // MemberBuilder
    @Builder
    public Member(String memberName, String memberProvider, String memberProviderEmail, String memberProfileImageUrl) {
        this.memberName = memberName;
        this.memberProvider = memberProvider;
        this.memberProviderEmail =memberProviderEmail;
        this.memberProfileImageUrl = memberProfileImageUrl;
    }

    // MemberUpdate
    public Member updateMemberName(String memberName) {
        this.memberName = memberName;

        return this;
    }
}
