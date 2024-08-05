package com.ssafy.freezetag.domain.balanceresult.service.request;

import com.ssafy.freezetag.domain.oxresult.entity.SelectedOption;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BalanceResultSaveRequestDto {

    // BalanceQuestion id -> 식별키
    private  String balanceQuestionId;

    private  SelectedOption balanceResultSelectedOption;
}
