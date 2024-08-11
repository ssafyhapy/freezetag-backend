package com.ssafy.freezetag.domain.member.service;

import static com.ssafy.freezetag.domain.member.entity.Visibility.PRIVATE;

import com.ssafy.freezetag.domain.exception.custom.InvalidMemberVisibilityException;
import com.ssafy.freezetag.domain.exception.custom.MemberNotFoundException;
import com.ssafy.freezetag.domain.exception.custom.MemberNotPublicException;
import com.ssafy.freezetag.domain.exception.custom.TokenException;
import com.ssafy.freezetag.domain.member.entity.Member;
import com.ssafy.freezetag.domain.member.entity.MemberHistory;
import com.ssafy.freezetag.domain.member.entity.Visibility;
import com.ssafy.freezetag.domain.member.repository.MemberHistoryRepository;
import com.ssafy.freezetag.domain.member.repository.MemberRepository;
import com.ssafy.freezetag.domain.member.service.helper.MemberConverter;
import com.ssafy.freezetag.domain.member.service.request.MypageModifyRequestDto;
import com.ssafy.freezetag.domain.member.service.response.MemberHistoryDto;
import com.ssafy.freezetag.domain.member.service.response.MemberMemoryboxDto;
import com.ssafy.freezetag.domain.member.service.response.MypageResponseDto;
import com.ssafy.freezetag.domain.member.service.response.MypageVisibilityResponseDto;
import com.ssafy.freezetag.domain.member.service.response.ProfileResponseDto;
import com.ssafy.freezetag.domain.oauth2.TokenProvider;
import com.ssafy.freezetag.domain.oauth2.service.TokenService;
import com.ssafy.freezetag.domain.room.entity.MemberRoom;
import com.ssafy.freezetag.domain.room.repository.MemberRoomRepository;
import com.ssafy.freezetag.global.s3.S3UploadService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final TokenProvider tokenProvider;
    private final TokenService tokenService;
    private final MemberRepository memberRepository;
    private final MemberHistoryRepository memberHistoryRepository;
    private final S3UploadService s3UploadService;
    private final MemberRoomRepository memberRoomRepository;

    /*
        member 찾는 부분 메소드화
     */
    public Member findMember(Long memberId) {
        return memberRepository
                .findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("멤버가 존재하지 않습니다."));
    }

    /*
        memberId를 통해서 ResponseDto 생성
     */
    public MypageResponseDto getMypage(Long memberId) {
        Member member = memberHistoryRepository.findMemberWithHistories(memberId);
        // 멤버 없을 시 예외처리
        if(member == null) {
            throw new MemberNotFoundException("회원이 존재하지 않습니다.");
        }
        log.info("member: {}", member.toString());
        List<MemberHistory> memberHistories = member.getMemberHistories();

        List<MemberRoom> memberRooms = memberRoomRepository.findAllByMemberIdWithFetchJoinRoom(memberId);
        return MemberConverter.createMypageResponseDto(
                member,
                memberHistories,
                memberRooms
        );

    }

    /*
        memberId를 통해서 마이페이지 프로필 공개, 비공개 설정
     */
    @Transactional
    public MypageVisibilityResponseDto setMypageVisibility(Long memberId, Visibility requestVisibility) {
        Member member = findMember(memberId);
        Visibility memberVisibility = member.getMemberVisibility();

        if (memberVisibility != requestVisibility) {
            throw new InvalidMemberVisibilityException("멤버 공개 정보가 일치하지 않습니다.");
        }

        member.updateMemberVisibility();

        return new MypageVisibilityResponseDto(
                member.getMemberVisibility());
    }

    @Transactional
    public void updateMemberHistory(Long memberId, MypageModifyRequestDto requestDto, final MultipartFile memberProfileImage) throws IOException {

        // 필요한 데이터 로드
        Member member = findMember(memberId);

        // 멤버 소개 페이지 업데이트
        member.updateMemberIntroduction(requestDto.getMemberIntroduction());

        if (memberProfileImage != null) {
            log.info("프로필 이미지 변경 감지");
            String s3Url = s3UploadService.saveFile(memberProfileImage);
            member.updateMemberProfileImageUrl(s3Url);
        }

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

    /*
        memberId를 통해서 ResponseDto 생성
     */
    public ProfileResponseDto getProfile(Long memberId) {

        Member member = memberHistoryRepository.findMemberWithHistories(memberId);

        // 멤버 없을 시 예외처리
        if(member == null) {
            throw new MemberNotFoundException("회원이 존재하지 않습니다.");
        }
        log.info("member: {}", member.toString());


        List<MemberHistory> memberHistories = member.getMemberHistories();


        // 만약 비공개라면
        if(member.getMemberVisibility().equals(PRIVATE)) {
            throw new MemberNotPublicException("프로필 비공개 멤버입니다.");
        }

        List<MemberRoom> memberRooms = memberRoomRepository.findAllByMemberIdWithFetchJoinRoom(memberId);

        // 반환
        return MemberConverter.createProfileResponseDto(
                member,
                memberHistories,
                memberRooms
        );
    }
}
