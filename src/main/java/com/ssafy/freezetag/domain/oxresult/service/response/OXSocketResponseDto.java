package com.ssafy.freezetag.domain.oxresult.service.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OXSocketResponseDto {
    private Long memberId;
    private String memberName;
    private String content;
    private boolean answer;
}
