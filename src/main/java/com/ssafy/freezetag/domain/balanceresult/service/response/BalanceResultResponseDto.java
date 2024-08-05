package com.ssafy.freezetag.domain.balanceresult.service.response;

import com.ssafy.freezetag.domain.oxresult.entity.SelectedOption;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BalanceResultResponseDto {
    private String memberName;

    private SelectedOption balanceResultSelectedOption;
}
