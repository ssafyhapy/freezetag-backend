package com.ssafy.freezetag.domain.member.service.response;

import com.ssafy.freezetag.domain.member.entity.Visibility;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MypageVisibilityResponseDto {
    private  Visibility visibility; // 0 = 비공개, 1 = 공개
}
