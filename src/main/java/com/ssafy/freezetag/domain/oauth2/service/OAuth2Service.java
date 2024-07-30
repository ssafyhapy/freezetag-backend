package com.ssafy.freezetag.domain.oauth2.service;

import com.ssafy.freezetag.domain.exception.custom.TokenException;
import com.ssafy.freezetag.domain.oauth2.TokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class OAuth2Service {

    private final TokenProvider tokenProvider;

    /*
        refreshToken으로 accessToken 재발급 후 refreshToken 갱신 메소드
     */
    public List<String> reissueAllTokens(HttpServletRequest request) {
        List<String> tokenList = new ArrayList<>();
        String accessToken;
        String refreshToken;
        Cookie[] cookies = request.getCookies();

        // 쿠키가 아예 존재하지 않나 확인
        if(cookies == null) {
            throw new TokenException("쿠키가 존재하지 않습니다.");
        }

        // 쿠키 조회
        refreshToken = getRefreshToken(cookies);
        if(!StringUtils.hasText(refreshToken)) {
            throw new TokenException("Refresh Token이 존재하지 않습니다.");
        }

        // Token 유효성 검증
        tokenProvider.validateToken(refreshToken);

        // memberId 추출
        Authentication authentication = tokenProvider.getAuthentication(refreshToken);
        String memberId = authentication.getName();
        accessToken = tokenProvider.reissueAccessToken(memberId);
        refreshToken = tokenProvider.generateRefreshToken(authentication);

        tokenList.add(accessToken);
        tokenList.add(refreshToken);
        return tokenList;
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
