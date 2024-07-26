package com.ssafy.freezetag.domain.result.service.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BalanceQuestionResponseDto {

    private final String optionFirst;

    private final String optionSecond;
}
