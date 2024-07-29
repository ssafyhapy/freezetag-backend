package com.ssafy.freezetag.domain.member.controller;

import com.ssafy.freezetag.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestParam String providerId) {

        // 우선 정상적인 provider인지 확인하고 리디렉션 URL을 가져옴
        // TODO : redirectUrl 관리하는 법 처리
        String redirectUrl = memberService.checkProvider(providerId);
        log.info("redirect url : {}", redirectUrl);

        // oauth url로 redirect 처리
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", redirectUrl)
                .build();
    }
}


