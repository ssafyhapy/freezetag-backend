package com.ssafy.freezetag.domain.oauth2.service;

import com.ssafy.freezetag.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

/*
    OAuth2 인증을 통해 얻은 사용자 정보를 저장하고 관리하기 위한 데이터 구조
 */
@Getter
public class OAuthAttributesDto {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String memberName;
    private String memberProvider;
    private String memberProviderEmail;
    private String memberProfileImageUrl;

    @Builder
    public OAuthAttributesDto(Map<String, Object> attributes,
                              String nameAttributeKey,
                              String memberName,
                              String memberProvider,
                              String memberProviderEmail,
                              String memberProfileImageUrl) {

        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.memberName = memberName;
        this.memberProvider = memberProvider;
        this.memberProviderEmail = memberProviderEmail;
        this.memberProfileImageUrl = memberProfileImageUrl;
    }

    /*
        OAuth가 여러 개이면 후에 분기하는 코드
     */
    public static OAuthAttributesDto of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if("kakao".equals(registrationId)) {
            return ofKakao("id", registrationId, attributes);
        }

        // TODO : 나중에 삭제
        return ofGoogle(userNameAttributeName, attributes);
    }

    // TODO : 나중에 삭제
    private static OAuthAttributesDto ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributesDto.builder()
                .memberName((String) attributes.get("name"))
                .memberProviderEmail((String) attributes.get("email"))
                .memberProfileImageUrl((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributesDto ofKakao(String userNameAttributeName, String registrationId, Map<String, Object> attributes) {
        // 카카오 정보 가져옴
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

        // 프로필 정보 가져옴
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        // 개별 정보 가져옴
        String nickname = (String) profile.get("nickname");
        String profileImageUrl = (String) profile.get("profile_image_url");
        String providerEmail = (String) kakaoAccount.get("email");

        // 빌드 후 OAuthAttributes object 반환
        return OAuthAttributesDto.builder()
                .memberName(nickname)
                .memberProfileImageUrl(profileImageUrl)
                .memberProviderEmail(providerEmail)
                .memberProvider(registrationId)
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }


    public Member toEntity() {
        return Member.builder()
                .memberName(memberName)
                .memberProviderEmail(memberProviderEmail)
                .memberProvider(memberProvider)
                .memberProfileImageUrl(memberProfileImageUrl)
                .build();
    }
}