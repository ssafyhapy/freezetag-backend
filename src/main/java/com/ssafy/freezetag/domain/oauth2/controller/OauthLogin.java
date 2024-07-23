package com.ssafy.freezetag.domain.oauth2.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class OauthLogin {

    private static final Logger log = LoggerFactory.getLogger(OauthLogin.class);
    private static final String SUCCESS_CODE = "200";
    private static final String SUCCESS_MSG = "Success";

    @GetMapping("/oauth2/kakao")
    public ResponseEntity<Map<String, Object>> kakaoLogin(@RequestParam String code) {
        String answer = "";
        log.debug("[+| Kakao Login AccessToken :: " + code);

        Map<String, Object> response = new HashMap<>();
        response.put("result", answer);
        response.put("resultCode", SUCCESS_CODE);
        response.put("resultMsg", SUCCESS_MSG);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 임시 페이지
    // TODO : 삭제
    @GetMapping("/oauth2/123")
    @ResponseBody
    public String hello() {
        return "<html>" +
                "<head><title>Temporary Page</title></head>" +
                "<body>" +
                "<h1>This is a temporary page</h1>" +
                "<p>Welcome to the temporary page.</p>" +
                "</body>" +
                "</html>";
    }

    // 로그인 페이지
    @GetMapping("/login")
    @ResponseBody
    public String login() {
        return "<html>" +
                "<head><title>Login Page</title></head>" +
                "<body>" +
                "<h1>Login</h1>" +
                "<a href=https://kauth.kakao.com/oauth/authorize?client_id=34935559ad5a5358e1cf4920274841c6&redirect_uri=http://localhost:8080/oauth2/kakao&response_type=code>카카오로그인>/a>" +
                "</form>" +
                "</body>" +
                "</html>";
    }
}
