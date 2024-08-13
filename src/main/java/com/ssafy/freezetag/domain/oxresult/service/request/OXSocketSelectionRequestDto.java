package com.ssafy.freezetag.domain.oxresult.service.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OXSocketSelectionRequestDto {
    private Long memberId;
    private boolean answer;
}
