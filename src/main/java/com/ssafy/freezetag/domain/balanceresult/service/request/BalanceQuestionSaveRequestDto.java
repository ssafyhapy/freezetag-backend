package com.ssafy.freezetag.domain.balanceresult.service.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BalanceQuestionSaveRequestDto {
    private Long roomId;

    private String optionFirst;

    private String optionSecond;
}
