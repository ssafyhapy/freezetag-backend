package com.ssafy.freezetag.domain.member.service.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MypageVisibilityResponseDto {
    private Boolean visibility; // 0 = 비공개, 1 = 공개
}
