package com.ssafy.freezetag.domain.member.controller;
import com.ssafy.freezetag.domain.member.service.request.MypageVisibilityRequestDto;
import com.ssafy.freezetag.global.argumentresolver.Login;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "회원 관리", description = "회원 관리 API")
public interface MemberControllerSwagger {

    @Operation(summary = "마이페이지 조회", description = "로그인한 사용자의 마이페이지 정보를 조회.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/mypage")
    ResponseEntity<?> mypage(@Login Long memberId);

    @Operation(summary = "마이페이지 수정", description = "사용자의 마이페이지 정보를 수정. 프로필 이미지와 관련된 파일도 업로드 가능.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "수정 완료", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content(mediaType = "application/json"))
    })
    @PatchMapping(value = "/mypage", consumes = "multipart/form-data")
    ResponseEntity<?> modifyMypage(
            @Login Long memberId,
            @RequestPart("memberName") String memberName,
            @RequestPart("memberProviderEmail") String memberProviderEmail,
            @RequestPart("memberIntroduction") String memberIntroduction,
            @RequestPart(value = "memberProfileImage", required = false) MultipartFile memberProfileImage,
            @RequestPart(required = false) String deletedHistoryList,
            @RequestPart(required = false) String memberHistoryList,
            @RequestPart(required = false) String memberMemoryboxList) throws IOException;

    @Operation(summary = "마이페이지 공개 범위 설정", description = "사용자의 마이페이지 공개 범위를 설정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "설정 완료", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/mypage/visibility")
    ResponseEntity<?> visibility(@Login Long memberId, @RequestBody MypageVisibilityRequestDto requestDto);

    @Operation(summary = "로그아웃", description = "현재 로그인된 사용자를 로그아웃 시키고 세션 쿠키를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "로그아웃 완료", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/logout")
    ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response);

    @Operation(summary = "프로필 조회", description = "다른 사용자의 프로필 정보를 조회합.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "회원이 존재하지 않음", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{memberId}")
    ResponseEntity<?> profile(@PathVariable Long memberId);
}
