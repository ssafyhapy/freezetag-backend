package com.ssafy.freezetag.domain.member.service;

import org.springframework.stereotype.Service;

@Service
public class MemberService {

    public String checkProvider(String providerId) {

        // provider id가 null이거나, providerId가 비었을 경우
        if (providerId == null || providerId.isEmpty()) {
            throw new RuntimeException("provider 값이 존재하지 않습니다.");
        }

        // 현재 어플리케이션에서 지원하는 provider인지 분기
        if (providerId.equals("kakao")) {
            return "/oauth2/authorization/kakao";
        }

        throw new RuntimeException("올바른 provider가 아닙니다.");
    }
}
