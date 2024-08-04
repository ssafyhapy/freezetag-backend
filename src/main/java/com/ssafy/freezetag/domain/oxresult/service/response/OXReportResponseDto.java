package com.ssafy.freezetag.domain.oxresult.service.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class OXReportResponseDto {
    private final String memberName;

    private final List<OXResponseDto> oxResponseDtos;
}
