package com.ssafy.freezetag.domain.balanceresult.service.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BalanceReportResponseDto {
    private String balanceQuestionOptionFirst;

    private String balanceQuestionOptionSecond;

    private List<BalanceResultResponseDto> balanceResultResponseDtos;
}
