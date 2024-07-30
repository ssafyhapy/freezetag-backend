package com.ssafy.freezetag.domain.oauth2.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.freezetag.domain.member.service.response.LoginResponseDto;
import com.ssafy.freezetag.domain.oauth2.TokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

import static com.ssafy.freezetag.domain.common.constant.TokenKey.TOKEN_PREFIX;

@RequiredArgsConstructor
@Component
@Slf4j
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 직렬화를 위한 ObjectMapper

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // accessToken, refreshToken 발급
        String accessToken = tokenProvider.generateAccessToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(authentication);

        // AccessToken 첨부
        response.addHeader("Authorization", TOKEN_PREFIX + accessToken);

        // HTTP-only 쿠키에 refreshToken 첨부
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true); // 쿠키를 HTTP-only로 설정
        cookie.setSecure(true); // HTTPS에서만 쿠키를 전송하도록 설정
        cookie.setPath("/"); // 쿠키의 유효 범위를 설정
        cookie.setMaxAge(3600); // 쿠키의 유효 기간 설정 (지금은 1시간)

        // 쿠키를 응답에 추가
        response.addCookie(cookie);

        // 응답 상태 코드를 201 Created로 설정
        response.setStatus(HttpServletResponse.SC_CREATED);

        // 응답 본문에 JSON 형태로 토큰 정보를 포함
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        // TODO : 이름 수정
        // LoginResponseDto 생성

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 'properties' 객체에서 'nickname'을 가져오는 과정
        @SuppressWarnings("unchecked")
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        String memberName = (String) properties.get("nickname");

        LoginResponseDto loginResponseDto = new LoginResponseDto(memberName);

        // Result 객체 생성
        Result<LoginResponseDto> result = new Result<>(true, loginResponseDto);

        // JSON 형태로 변환
        String jsonResponse = objectMapper.writeValueAsString(result);

        // 응답에 JSON 작성
        response.getWriter().write(jsonResponse);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private boolean success;
        private T data;
    }
}
