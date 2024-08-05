package com.ssafy.freezetag.domain.oxresult.service.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OXReportResponseDto {
    private String memberName;

    private List<OXResponseDto> oxResponseDtos;
}