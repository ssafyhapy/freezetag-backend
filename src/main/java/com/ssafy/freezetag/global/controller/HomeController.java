package com.ssafy.freezetag.global.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import java.util.Map;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
public class HomeController {

    @GetMapping("/")
    public String homePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {

            if (authentication.getPrincipal() instanceof OAuth2User) {
                OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
                Map<String, Object> attributes = oAuth2User.getAttributes();

                // 'properties' 객체에서 'nickname'을 가져오는 과정
                @SuppressWarnings("unchecked")
                Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
                String nickname = (String) properties.get("nickname");
                model.addAttribute("nickname", nickname);
            }
        }
        return "home"; // home.mustache 파일을 참조
    }

    @GetMapping("/login/oauth2/code/kakao")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> redirectPage(@RequestParam String code) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/oauth/login";

        Map<String, String> request = Map.of(
                "registrationId", "kakao",
                "authorizationCode", code
        );

        // HTTP 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

        // POST 요청 보내고 ResponseEntity로 받기
        ResponseEntity<Map> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

        // ResponseEntity의 모든 요소를 반환
        return new ResponseEntity<>(responseEntity.getBody(), responseEntity.getHeaders(), responseEntity.getStatusCode());
    }
}
