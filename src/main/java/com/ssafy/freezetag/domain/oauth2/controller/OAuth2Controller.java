package com.ssafy.freezetag.domain.oauth2.controller;

import com.ssafy.freezetag.domain.oauth2.service.OAuth2Service;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class OAuth2Controller {

    private final OAuth2Service oAuth2Service;

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
