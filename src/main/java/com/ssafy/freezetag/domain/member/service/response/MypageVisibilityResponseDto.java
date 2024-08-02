package com.ssafy.freezetag.domain.member.service.response;

import com.ssafy.freezetag.domain.member.entity.Visibility;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MypageVisibilityResponseDto {
    private final Visibility visibility; // 0 = 비공개, 1 = 공개
}
