package com.ssafy.freezetag.domain.oauth2;

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
import org.springframework.util.StringUtils;
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

        // accessToken 검증
        if (tokenProvider.validateToken(accessToken)) {
            log.info("AccessToken is valid!!");
            setAuthentication(accessToken); // 토큰이 유효하므로 인증 정보 설정
        } else {
            log.info("AccessToken not valid");
            // 우선 userId를 AccessToken으로 추출


            if (StringUtils.hasText(accessToken)) {
                String memberId = tokenProvider.getAuthentication(accessToken).getName();
                log.info("memberId : {}", memberId);
                // 만료되었을 경우 accessToken 재발급
                String reissueAccessToken = tokenProvider.reissueAccessToken(memberId);
                log.info("reissueAccessToken : {}", reissueAccessToken);
                if (StringUtils.hasText(reissueAccessToken)) {
                    setAuthentication(reissueAccessToken);

                    // 재발급된 accessToken 다시 전달
                    response.setHeader(AUTHORIZATION, TokenKey.TOKEN_PREFIX + reissueAccessToken);
                }
            }


        }

        filterChain.doFilter(request, response);
    }

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