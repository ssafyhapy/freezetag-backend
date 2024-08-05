package com.ssafy.freezetag.domain.introresult.service.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class IntroReportResponseDto {
    private String memberName;

    private String content;
}