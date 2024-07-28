package com.ssafy.freezetag.domain.oauth2.service;

import com.ssafy.freezetag.domain.member.entity.Member;
import com.ssafy.freezetag.domain.member.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

/*
    OAuth 라이브러리 활용 로그인 Service
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    private final HttpSession httpSession;

    /*
        OAuth 로그인 요청이 오면 인가 코드부터 회원정보까지 볼 수 있는 코드
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // OAuth 제공자의 ID를 가져옴

        log.info("oauth2user: {}", oAuth2User);
        log.info("registration_id : {}", registrationId);

        // 유저를 고유로 식별할 수 있는 키 (ex. kakao : id)
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributesDto attributes = OAuthAttributesDto.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        log.info("userNameAttributeName : {}", userNameAttributeName);
        log.info("attributes : {}", attributes.toString());

        // 여기서 이제 attributes를  활용해서 DB에 회원가입 or login 진행
        Member member = saveOrUpdate(attributes);
        httpSession.setAttribute("member", new SessionMemberDto(member));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
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
}
