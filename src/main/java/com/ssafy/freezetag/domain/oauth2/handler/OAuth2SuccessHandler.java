package com.ssafy.freezetag.domain.oauth2.handler;

import com.ssafy.freezetag.domain.oauth2.TokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private static final String URI = "/";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // accessToken, refreshToken 발급
        String accessToken = tokenProvider.generateAccessToken(authentication);
        // refreshToken 생성 후 redis에 저장
        tokenProvider.generateRefreshToken(authentication);

        // HTTP-only 쿠키로 accessToken 설정
        Cookie cookie = new Cookie("accessToken", accessToken);
        cookie.setHttpOnly(true); // 쿠키를 HTTP-only로 설정
        cookie.setSecure(true); // HTTPS에서만 쿠키를 전송하도록 설정
        cookie.setPath("/"); // 쿠키의 유효 범위를 설정
        cookie.setMaxAge(3600); // 쿠키의 유효 기간 설정 (지금은 1시간)

        // 쿠키를 응답에 추가
        response.addCookie(cookie);

        // 토큰 전달을 위한 redirect
        String redirectUrl = UriComponentsBuilder.fromUriString(URI).build().toUriString();
        response.sendRedirect(redirectUrl);
    }
}
