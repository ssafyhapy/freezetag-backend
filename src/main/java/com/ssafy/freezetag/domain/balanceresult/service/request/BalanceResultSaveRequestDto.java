package com.ssafy.freezetag.domain.balanceresult.service.request;

import com.ssafy.freezetag.domain.oxresult.entity.SelectedOption;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BalanceResultSaveRequestDto {

    // BalanceQuestion id -> 식별키
    private final String balanceQuestionId;

    private final Long memberId;

    private final SelectedOption balanceResultSelectedOption;
}
