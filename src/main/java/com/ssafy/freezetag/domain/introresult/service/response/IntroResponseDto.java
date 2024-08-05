package com.ssafy.freezetag.domain.introresult.service.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class IntroResponseDto {
    private  String id;

    private  Long memberId;

    private  String content;
}
