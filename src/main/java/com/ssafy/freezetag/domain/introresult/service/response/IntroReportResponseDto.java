package com.ssafy.freezetag.domain.introresult.service.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class IntroReportResponseDto {
    private final String memberName;

    private final String content;
}
