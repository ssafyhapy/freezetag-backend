package com.ssafy.freezetag.global.filter;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.ssafy.freezetag.domain.common.constant.TokenKey;
import com.ssafy.freezetag.domain.oauth2.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Component
@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String accessToken = resolveToken(request);
        log.info("accesstoken: {}", accessToken);
        // 1. 우선 filter을 통해 accessToken에 이상이 없을 경우
        if (tokenProvider.validateToken(accessToken)) {
            log.info("AccessToken is valid!!");
            setAuthentication(accessToken); // 토큰이 유효하므로 인증 정보 설정
        }
        filterChain.doFilter(request, response);
    }

    /*
        인증정보 설정하는 메소드
     */
    private void setAuthentication(String accessToken) {
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /*
        요청 헤더에서 토큰 추출
     */
    private String resolveToken(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION);
        if (ObjectUtils.isEmpty(token) || !token.startsWith(TokenKey.TOKEN_PREFIX)) {
            return null;
        }
        return token.substring(TokenKey.TOKEN_PREFIX.length());
    }
}