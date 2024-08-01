package com.ssafy.freezetag.domain.member.service;

import com.ssafy.freezetag.domain.exception.custom.TokenException;
import com.ssafy.freezetag.domain.member.entity.Member;
import com.ssafy.freezetag.domain.member.entity.MemberHistory;
import com.ssafy.freezetag.domain.member.entity.MemberMemorybox;
import com.ssafy.freezetag.domain.member.repository.MemberHistoryRepository;
import com.ssafy.freezetag.domain.member.repository.MemberMemoryboxRepository;
import com.ssafy.freezetag.domain.member.repository.MemberRepository;
import com.ssafy.freezetag.domain.member.service.response.MemberHistoryDto;
import com.ssafy.freezetag.domain.member.service.response.MemberMemoryboxDto;
import com.ssafy.freezetag.domain.member.service.response.MypageResponseDto;
import com.ssafy.freezetag.domain.oauth2.TokenProvider;
import com.ssafy.freezetag.domain.oauth2.service.TokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final TokenProvider tokenProvider;
    private final TokenService tokenService;
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberHistoryRepository memberHistoryRepository;

    @Autowired
    private MemberMemoryboxRepository memberMemoryboxRepository;

    /*
        memberId를 통해서 ResponseDto 생성
     */
    public MypageResponseDto getMyPage(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        List<MemberHistoryDto> memberHistoryList = memberHistoryRepository.findByMemberId(memberId).stream()
                .map(history -> MemberHistoryDto.builder()
                        .memberHistoryDate(history.getMemberHistoryDate())
                        .memberHistoryContent(history.getMemberHistoryContent())
                        .build())
                .collect(Collectors.toList());

        List<MemberMemoryboxDto> memberMemoryboxList = memberMemoryboxRepository.findByMemberId(memberId).stream()
                .map(memorybox -> MemberMemoryboxDto.builder()
                        .memberHistoryDate(memorybox.getMemberHistoryDate())
                        .memberHistoryContent(memorybox.getMemberHistoryContent())
                        .thumbnail(memorybox.getThumbnail())
                        .photo(memorybox.getPhoto())
                        .build())
                .collect(Collectors.toList());

        return MypageResponseDto.builder()
                .memberName(member.getMemberName())
                .memberProviderEmail(member.getMemberProviderEmail())
                .memberProfileImageUrl(member.getMemberProfileImageUrl())
                .memberIntroduction(member.getMemberIntroduction())
                .memberHistoryList(memberHistoryList)
                .memberMemoryboxList(memberMemoryboxList)
                .build();
    }

    public String checkProvider(String providerId) {

        // provider id가 null이거나, providerId가 비었을 경우
        if (providerId == null || providerId.isEmpty()) {
            throw new RuntimeException("provider 값이 존재하지 않습니다.");
        }

        // 현재 어플리케이션에서 지원하는 provider인지 분기
        if (providerId.equals("kakao")) {
            return "/oauth2/authorization/kakao";
        }

        throw new RuntimeException("올바른 provider가 아닙니다.");
    }

    /*
        로그아웃 => token redis에서 삭제
     */
    // TODO : OAuth2Controller과 겹친 부분 utils로 빼기
    public void checkAuthentication(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        // 쿠키가 아예 존재하지 않나 확인
        if(cookies == null) {
            throw new RuntimeException("쿠키가 존재하지 않습니다.");
        }

        // 쿠키 조회
        String refreshToken = getRefreshToken(cookies);
        if(!StringUtils.hasText(refreshToken)) {
            throw new TokenException("Refresh Token이 존재하지 않습니다.");
        }

        // accessToken 조회
        String accessToken = request.getHeader("Authorization");



        // 그리고 access, refresh간 id 불일치 발생하면 처리
        if(!tokenProvider.validateSameTokens(accessToken, refreshToken)) {
            throw new TokenException("Access Token과 Refresh Token 사용자 정보 불일치합니다.");
        }

        Long memberId = tokenProvider.getMemberIdFromToken(refreshToken);
        // 레디스에서 정보 삭제
        tokenService.deleteRefreshToken(memberId.toString());

    }

    /*
        레디스의 refreshToken, 세션 정보 삭제
     */
    public void deleteToken(HttpServletRequest request, HttpServletResponse response) {

        // refreshToken 쿠키 삭제
        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setMaxAge(0); // 쿠키의 유효 기간을 0으로 설정하여 삭제
        refreshTokenCookie.setPath("/"); // 쿠키가 유효한 경로 설정
        response.addCookie(refreshTokenCookie);

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
