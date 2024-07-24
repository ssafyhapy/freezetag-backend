package com.ssafy.freezetag.domain.oauth2.service;

import com.ssafy.freezetag.domain.member.entity.Member;

import java.io.Serializable;

/*
    직렬화 기능을 가진 세션DTO
 */
public class SessionMemberDto implements Serializable {
    private String name;
    private String provider;
    private String email;
    private String picture;

    public SessionMemberDto(Member member) {
        this.name = member.getMemberName();
        this.provider = member.getMemberProvider();
        this.email = member.getMemberProviderEmail();
        this.picture = member.getMemberProfileImageUrl();
    }
}
