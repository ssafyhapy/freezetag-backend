package com.ssafy.freezetag.domain.balanceresult.service.response;

import com.ssafy.freezetag.domain.balanceresult.entity.SelectedOption;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BalanceResultSaveResponseDto {
    private Long memberId;
    private SelectedOption balanceResultSelectedOption;
}
