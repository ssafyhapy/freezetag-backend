package com.ssafy.freezetag.domain.member.service;

import com.ssafy.freezetag.domain.exception.custom.TokenException;
import com.ssafy.freezetag.domain.oauth2.TokenProvider;
import com.ssafy.freezetag.domain.oauth2.entity.Token;
import com.ssafy.freezetag.domain.oauth2.service.TokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final TokenProvider tokenProvider;
    private final TokenService tokenService;

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

    /*
        로그아웃 => token redis에서 삭제
     */
    // TODO : OAuth2Controller과 겹친 부분 utils로 빼기
    public void checkAuthentication(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        // 쿠키가 아예 존재하지 않나 확인
        if(cookies == null) {
            throw new TokenException("쿠키가 존재하지 않습니다.");
        }

        // 쿠키 조회
        String refreshToken = getRefreshToken(cookies);
        if(!StringUtils.hasText(refreshToken)) {
            throw new TokenException("Refresh Token이 존재하지 않습니다.");
        }

        // accessToken 조회
        String accessToken = request.getHeader("Authorization");
//
//
//        // 인증 정보 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new RuntimeException("현재 사용자의 인증 정보 오류가 발생하였습니다.");
        }

        String memberId = authentication.getName();
        // 그리고 access, refresh간 id 불일치 발생하면 처리
        if(!tokenProvider.validateSameTokens(accessToken, refreshToken)) {
            throw new TokenException("Access Token과 Refresh Token 사용자 정보 불일치합니다.");
        }

        System.out.println(memberId);
        // 현재 시큐리티 세션의 id랑 accessToken 불일치 발생하면 오류
        if(!tokenProvider.getAuthentication((accessToken)).getName().equals(memberId)) {
            throw new TokenException("유저와 사용자 정보가 불일치합니다.");
        }
        // 레디스에서 정보 삭제
        tokenService.deleteRefreshToken(memberId);

    }

    /*
        레디스의 refreshToken, 세션 정보 삭제
     */
    public void deleteTokenAndSession(HttpServletRequest request, HttpServletResponse response) {
        // 로그아웃 후 세션 무효화 및 쿠키 삭제
        request.getSession().invalidate();

        // refreshToken 쿠키 삭제
        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setMaxAge(0); // 쿠키의 유효 기간을 0으로 설정하여 삭제
        refreshTokenCookie.setPath("/"); // 쿠키가 유효한 경로 설정
        response.addCookie(refreshTokenCookie);

        // 세션 쿠키 삭제
        Cookie sessionCookie = new Cookie("JSESSIONID", null);
        sessionCookie.setPath("/"); // 쿠키의 유효 경로 설정
        sessionCookie.setMaxAge(0); // 쿠키의 유효 기간을 0으로 설정하여 삭제
        response.addCookie(sessionCookie);
    }

    /*
    쿠키에서 refreshToken 찾아주는 코드
 */
    public String getRefreshToken(Cookie[] cookies) {
        String refreshToken = "";
        for (Cookie cookie : cookies) {
            if ("refreshToken".equals(cookie.getName())) {
                refreshToken = cookie.getValue();
                break;
            }
        }
        return refreshToken;
    }
}
