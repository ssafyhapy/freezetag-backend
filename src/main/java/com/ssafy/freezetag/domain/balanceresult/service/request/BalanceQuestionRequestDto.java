package com.ssafy.freezetag.domain.balanceresult.service.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BalanceQuestionRequestDto {
    private String purpose;

    public BalanceQuestionRequestDto(String purpose) {
        this.purpose = purpose;
    }
}
