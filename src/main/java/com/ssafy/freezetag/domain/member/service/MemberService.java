package com.ssafy.freezetag.domain.member.service;

import com.ssafy.freezetag.domain.exception.custom.InvalidMemberVisibilityException;
import com.ssafy.freezetag.domain.exception.custom.MemberNotFoundException;
import com.ssafy.freezetag.domain.exception.custom.TokenException;
import com.ssafy.freezetag.domain.member.entity.Member;
import com.ssafy.freezetag.domain.member.entity.MemberHistory;
import com.ssafy.freezetag.domain.member.repository.MemberHistoryRepository;
import com.ssafy.freezetag.domain.member.repository.MemberMemoryboxRepository;
import com.ssafy.freezetag.domain.member.repository.MemberRepository;
import com.ssafy.freezetag.domain.member.service.request.MypageModifyRequestDto;
import com.ssafy.freezetag.domain.member.service.response.MemberHistoryDto;
import com.ssafy.freezetag.domain.member.service.response.MemberMemoryboxDto;
import com.ssafy.freezetag.domain.member.service.response.MypageResponseDto;
import com.ssafy.freezetag.domain.member.service.response.MypageVisibilityResponseDto;
import com.ssafy.freezetag.domain.oauth2.TokenProvider;
import com.ssafy.freezetag.domain.oauth2.service.TokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        member 찾는 부분 메소드화
     */
    private Member findMember(Long memberId) {
        return memberRepository
                .findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("멤버가 존재하지 않습니다."));
    }

    /*
        memberId를 통해서 ResponseDto 생성
     */
    public MypageResponseDto getMypage(Long memberId) {
        Member member = findMember(memberId);

        List<MemberHistoryDto> memberHistoryList = memberHistoryRepository.findByMemberId(memberId).stream()
                .map(history -> MemberHistoryDto.builder()
                        .memberHistoryId(history.getId())
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

    /*
        memberId를 통해서 마이페이지 프로필 공개, 비공개 설정
     */
    @Transactional
    public MypageVisibilityResponseDto setMypageVisibility(Long memberId, Boolean requestVisibility) {

        // DB에 있는 visibility 정보 로드
        Member member = findMember(memberId);
        Boolean memberVisibility = member.isMemberVisibility();

        // 만약 DB에 있는 정보랑 요청한 정보가 일치하다면
        if (!memberVisibility.equals(requestVisibility)) {
            throw new InvalidMemberVisibilityException("멤버 공개 정보가 일치하지 않습니다.");
        }

        // DB 반영
        member.updateMemberVisibility();

        // Toggle 느낌으로 구현
        return MypageVisibilityResponseDto.builder()
                .visibility(member.isMemberVisibility())
                .build();
    }

    @Transactional
    public void updateMemberHistory(Long memberId, MypageModifyRequestDto requestDto) {

        // 필요한 데이터 로드
        Member member = findMember(memberId);
        List<MemberHistoryDto> memberHistoryDtos = requestDto.getMemberHistoryList();
        // TODO : 추억상자 나중 구현
        List<MemberMemoryboxDto> memberMemoryboxDtos = requestDto.getMemberMemoryboxList();
        List<Long> deletedHistoryList = requestDto.getDeletedHistoryList();

        // 삭제 대상 지움
        if (deletedHistoryList != null && !deletedHistoryList.isEmpty()) {
            List<MemberHistory> historiesToDelete = memberHistoryRepository.findAllById(deletedHistoryList);
            memberHistoryRepository.deleteAll(historiesToDelete);
            member.getMemberHistories().removeAll(historiesToDelete);
        }
        // TODO : 한 번에 반복문 돌도록 진행하기!
        // 먼저 추가할 항목을 가져온다.
        List<MemberHistoryDto> addList = memberHistoryDtos.stream()
                .filter(dto -> dto.getMemberHistoryId() == -1) // 추가할 항목 ID
                .toList();

        for (MemberHistoryDto memberHistoryDto : addList) {
            MemberHistory newMemberHistory = new MemberHistory(
                    member,
                    memberHistoryDto.getMemberHistoryDate(),
                    memberHistoryDto.getMemberHistoryContent()
            );

            memberHistoryRepository.save(newMemberHistory);
            member.getMemberHistories().add(newMemberHistory);
        }

        // 현재의 모든 히스토리를 가져와서 업데이트합니다.
        for (MemberHistoryDto dto : memberHistoryDtos) {
            Long dtoId = dto.getMemberHistoryId();

            if (dtoId != -1) {
                Optional<MemberHistory> existingHistoryOpt = memberHistoryRepository.findById(dtoId);
                if (existingHistoryOpt.isPresent()) {
                    MemberHistory existingHistory = existingHistoryOpt.get();
                    existingHistory.setMemberHistoryDate(dto.getMemberHistoryDate());
                    existingHistory.setMemberHistoryContent(dto.getMemberHistoryContent());
                }
            }
        }

        // 변경된 엔티티를 저장합니다.
        memberRepository.save(member);
    }



    /*
        로그아웃 => token redis에서 삭제
     */
    // TODO : OAuth2Controller과 겹친 부분 utils로 빼기
    public void checkAuthentication(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        // 쿠키가 아예 존재하지 않나 확인
        if (cookies == null) {
            throw new RuntimeException("쿠키가 존재하지 않습니다.");
        }

        // 쿠키 조회
        String refreshToken = getRefreshToken(cookies);
        if (!StringUtils.hasText(refreshToken)) {
            throw new TokenException("Refresh Token이 존재하지 않습니다.");
        }

        // accessToken 조회
        String accessToken = request.getHeader("Authorization");


        // 그리고 access, refresh간 id 불일치 발생하면 처리
        if (!tokenProvider.validateSameTokens(accessToken, refreshToken)) {
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
