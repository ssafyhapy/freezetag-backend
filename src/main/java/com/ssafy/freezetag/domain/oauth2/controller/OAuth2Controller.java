package com.ssafy.freezetag.domain.oauth2.controller;


import com.ssafy.freezetag.domain.oauth2.TokenProvider;
import com.ssafy.freezetag.domain.oauth2.service.OAuth2Service;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class OAuth2Controller {

    private final OAuth2Service oAuth2Service;
    private final TokenProvider tokenProvider;

    /*
        front로부터 authorization 받으면 유저 정보를 얻어와 accessToken, refreshToken 보내주는 코드
     */
    @PostMapping("/login")
    public ResponseEntity<?> exchangeAuthorizationCode(@RequestBody Map<String, String> request, HttpServletResponse response) {

        // accessToken 발급
        String oAuthaccessToken = oAuth2Service.getAccessToken(request);
        // Retrieve user info using access token
        OAuth2User oAuth2User = oAuth2Service.loadUserByAccessToken(oAuthaccessToken, request);

        // Create OAuth2AuthenticationToken
        Authentication authentication = new OAuth2AuthenticationToken(oAuth2User, oAuth2User.getAuthorities(), "authorization");


        // accessToken, refreshToken 발급
        String accessToken = tokenProvider.generateAccessToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(authentication);

        // accessToken을 HTTP 헤더에 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        // refreshToken을 HTTP Only 쿠키로 설정
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true); // HTTPS에서만 사용 가능하도록 설정
        refreshTokenCookie.setPath("/"); // 쿠키가 유효한 경로 설정
        response.addCookie(refreshTokenCookie);

        // 응답 JSON 객체 생성
        var responseBody = new ResponseBody(true);

        // 응답 반환
        return ResponseEntity.ok()
                .headers(headers)
                .body(responseBody);
    }

    /*
        만료된 토큰 재발급하는 메소드
     */
    @GetMapping("/reissue-tokens")
    public ResponseEntity<?> reissueAllToken(HttpServletRequest request, HttpServletResponse response) {
        List<String> tokenList = oAuth2Service.reissueAllTokens(request);

        // 토큰을 리스트에서 추출
        String accessToken = tokenList.get(0);
        String refreshToken = tokenList.get(1);

        // accessToken을 HTTP 헤더에 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        // refreshToken을 HTTP Only 쿠키로 설정
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true); // HTTPS에서만 사용 가능하도록 설정
        refreshTokenCookie.setPath("/"); // 쿠키가 유효한 경로 설정
        response.addCookie(refreshTokenCookie);

        // 응답 JSON 객체 생성
        var responseBody = new ResponseBody(true);

        // 응답 반환
        return ResponseEntity.ok()
                .headers(headers)
                .body(responseBody);
    }

    // 응답 바디를 위한 내부 클래스
    @Getter
    private static class ResponseBody {
        private final boolean success;

        public ResponseBody(boolean success) {
            this.success = success;
        }

    }
}
