package com.ssafy.freezetag.domain.oauth2.service;

import com.ssafy.freezetag.domain.member.entity.Member;
import lombok.Getter;

import java.io.Serializable;

/*
    직렬화 기능을 가진 세션DTO
 */
@Getter
public class SessionMemberDto implements Serializable {
    private Long memberId;
    private String memberName;
    private String memberProvider;
    private String memberProviderEmail;
    private String memberProfileImageUrl;

    public SessionMemberDto(Member member) {
        this.memberId = member.getId();
        this.memberName = member.getMemberName();
        this.memberProvider = member.getMemberProvider();
        this.memberProviderEmail = member.getMemberProviderEmail();
        this.memberProfileImageUrl = member.getMemberProfileImageUrl();
    }
}
