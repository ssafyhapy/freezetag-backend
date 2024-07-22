package com.ssafy.freezetag.domain.member.entity;

import com.ssafy.freezetag.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.*;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class MemberVisibility extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "member_visibility_id")
    private Long id;

    private boolean isPublicIntroduction;
    private boolean isPublicHobby;
    private boolean isPublicHistory;
    private boolean isPublicInterest;

    public MemberVisibility(final boolean isPublicIntroduction, final boolean isPublicHobby, final boolean isPublicHistory, final boolean isPublicInterest) {
        this.isPublicIntroduction = true;
        this.isPublicHobby = true;
        this.isPublicHistory = true;
        this.isPublicInterest = true;
    }
}
