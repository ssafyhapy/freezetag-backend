package com.ssafy.freezetag.domain.member.controller;

import com.ssafy.freezetag.domain.member.service.MemberService;
import com.ssafy.freezetag.domain.oauth2.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    private final TokenService tokenService;

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

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        // 현재 로그인 인증정보 확인
        memberService.checkAuthentication(request);

        // 로그아웃 후 세션 무효화 및 쿠키 삭제
        memberService.deleteTokenAndSession(request, response);

        // 응답 JSON 객체 생성
        var responseBody = new MemberController.ResponseBody(true);

        // 응답 반환
        return ResponseEntity.ok()
                .body(responseBody);
    }

    // 응답 바디를 위한 내부 클래스
    private static class ResponseBody {
        private final boolean success;

        public ResponseBody(boolean success) {
            this.success = success;
        }

        public boolean isSuccess() {
            return success;
        }
    }
}


