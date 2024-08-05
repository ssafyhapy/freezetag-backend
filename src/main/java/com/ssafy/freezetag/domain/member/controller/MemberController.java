package com.ssafy.freezetag.domain.member.controller;

import com.ssafy.freezetag.domain.member.entity.Visibility;
import com.ssafy.freezetag.domain.member.repository.MemberRepository;
import com.ssafy.freezetag.domain.member.service.MemberService;
import com.ssafy.freezetag.domain.member.service.request.MypageModifyRequestDto;
import com.ssafy.freezetag.domain.member.service.request.MypageVisibilityRequestDto;
import com.ssafy.freezetag.domain.member.service.response.MypageResponseDto;
import com.ssafy.freezetag.domain.member.service.response.MypageVisibilityResponseDto;
import com.ssafy.freezetag.domain.oauth2.service.TokenService;
import com.ssafy.freezetag.global.argumentresolver.Login;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.ssafy.freezetag.domain.common.CommonResponse.success;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final TokenService tokenService;
    private final MemberRepository memberRepository;

    @GetMapping("/mypage")
    public ResponseEntity<?> mypage(@Login Long memberId) {

        MypageResponseDto mypageResponseDto = memberService.getMypage(memberId);
        log.info("response: {}", mypageResponseDto.toString());

        return ResponseEntity.ok()
                .body(success(mypageResponseDto));
    }

    @PatchMapping("/mypage")
    public ResponseEntity<?> modifyMypage(@Login Long memberId,
                                          @RequestBody MypageModifyRequestDto requestDto,
                                          @RequestParam(required = false) MultipartFile profileImage) throws IOException {

        memberService.updateMemberHistory(memberId, requestDto, profileImage);
        log.info("수정 완료!");

        return ResponseEntity.noContent()
                .build();
    }

    @PutMapping("/mypage/visibility")
    public ResponseEntity<?> visibility(@Login Long memberId, @RequestBody MypageVisibilityRequestDto requestDto) {
        Visibility requestVisibility = requestDto.getVisibility();
        MypageVisibilityResponseDto mypageVisibilityResponseDto = memberService.setMypageVisibility(memberId, requestVisibility);
        return ResponseEntity.ok()
                .body(success(mypageVisibilityResponseDto));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        // 현재 로그인 인증정보 확인
        memberService.checkAuthentication(request);

        // 로그아웃 후 세션 쿠키 삭제
        memberService.deleteToken(request, response);

        // 응답 JSON 객체 생성
        return ResponseEntity.noContent()
                .build();
    }
}


