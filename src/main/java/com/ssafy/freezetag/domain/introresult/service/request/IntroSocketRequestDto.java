package com.ssafy.freezetag.domain.introresult.service.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class IntroSocketRequestDto {
    private Long memberId;
    private String content;
}
