package com.ssafy.freezetag.global.controller;

import com.ssafy.freezetag.global.util.s3.S3UploadService;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Controller
public class HomeController {

    private final S3UploadService s3UploadService;

    public HomeController(final S3UploadService s3UploadService) {
        this.s3UploadService = s3UploadService;
    }

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

    @PostMapping(value = "/api/s3/test", consumes = "multipart/form-data")
    public ResponseEntity<?> test(@RequestParam("file") MultipartFile file) {
        try {
            String url = s3UploadService.saveFile(file);
            return ResponseEntity.ok(url);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed: " + e.getMessage());
        }
    }

}
