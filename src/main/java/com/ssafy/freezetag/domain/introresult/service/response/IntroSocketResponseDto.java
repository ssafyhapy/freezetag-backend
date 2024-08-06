package com.ssafy.freezetag.domain.introresult.service.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class IntroSocketResponseDto {
    private Long memberId;
    private String memberName;
    private String content;
}
