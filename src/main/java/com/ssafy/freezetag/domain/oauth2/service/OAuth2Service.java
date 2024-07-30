package com.ssafy.freezetag.domain.oauth2.service;

import com.ssafy.freezetag.domain.exception.custom.TokenException;
import com.ssafy.freezetag.domain.member.entity.Member;
import com.ssafy.freezetag.domain.member.repository.MemberRepository;
import com.ssafy.freezetag.domain.oauth2.TokenProvider;
import com.ssafy.freezetag.domain.oauth2.entity.CustomOAuth2User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class OAuth2Service {

    private final ClientRegistrationRepository clientRegistrationRepository;
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    /*
        authorization으로 access token을 발급받는 코드
     */
    public String getAccessToken(Map<String, String> request) {
        String registrationId = request.get("registrationId");
        String authorizationCode = request.get("authorizationCode");

        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(registrationId);
        if (clientRegistration == null) {
            throw new IllegalArgumentException("Invalid provider ID");
        }

        // Prepare the request to the token endpoint
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(clientRegistration.getProviderDetails().getTokenUri())
                .queryParam("grant_type", "authorization_code")
                .queryParam("code", authorizationCode)
                .queryParam("redirect_uri", clientRegistration.getRedirectUri())
                .queryParam("client_id", clientRegistration.getClientId())
                .queryParam("client_secret", clientRegistration.getClientSecret());

        // Make the request and extract the access token from the response
        Map<String, String> response = restTemplate.postForObject(uriBuilder.toUriString(), null, Map.class);
        System.out.println(response);
        String accessToken = response.get("access_token");

        if (accessToken == null) {
            throw new RuntimeException("Failed to obtain access token");
        }
        System.out.println("Access token: " + accessToken);
        return accessToken;
    }

    public OAuth2User loadUserByAccessToken(String accessToken, Map<String, String> request) {
        String registrationId = request.get("registrationId");
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(registrationId);
        if (clientRegistration == null) {
            throw new IllegalArgumentException("Invalid provider ID");
        }

        String userInfoEndpointUri = clientRegistration.getProviderDetails().getUserInfoEndpoint().getUri();

        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> userAttributes = restTemplate.getForObject(userInfoEndpointUri + "?access_token=" + accessToken, Map.class);

        if (userAttributes == null) {
            throw new RuntimeException("Failed to retrieve user info");
        }
        String userNameAttributeName = clientRegistration.getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        DefaultOAuth2User oAuth2User = new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                userAttributes,
                userNameAttributeName
        );

        OAuthAttributesDto attributes = OAuthAttributesDto.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());



        // 저장
        Member member = saveOrUpdate(attributes);

        // Return the user details (modify according to your needs)
        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                userAttributes,
                userNameAttributeName,
                member.getId()
        );
    }


    /*
    이미 존재하는 유저를 확인하는 메소드이며 신규 회원일 경우 통째로 entity 저장, 기존 회원일 경우, 닉네임만 수정하는 작업
    */
    private Member saveOrUpdate(OAuthAttributesDto attributes) {
        Member member = memberRepository.findByMemberProviderEmail(attributes.getMemberProviderEmail())
                .map(entity -> entity.updateMemberName(attributes.getMemberName()))
                .orElse(attributes.toEntity());

        return memberRepository.save(member);
    }

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
