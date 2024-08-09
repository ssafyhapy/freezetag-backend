package com.ssafy.freezetag.domain.oxresult.service.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OXSocketNextResponseDto {
    private Long memberId;
    private Integer nextIndex;
}
