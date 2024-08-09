package com.ssafy.freezetag.domain.member.service.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberReportResponseDto {
    private String memberName;

    private String memberProfileImageUrl;
}
