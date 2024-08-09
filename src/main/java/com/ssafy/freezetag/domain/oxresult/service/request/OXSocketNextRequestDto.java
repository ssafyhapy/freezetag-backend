package com.ssafy.freezetag.domain.oxresult.service.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OXSocketNextRequestDto {
    private Long memberId;
    private Integer nowIndex;
}
