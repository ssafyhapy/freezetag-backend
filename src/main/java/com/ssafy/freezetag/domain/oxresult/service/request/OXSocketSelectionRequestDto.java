package com.ssafy.freezetag.domain.oxresult.service.request;

import com.ssafy.freezetag.domain.oxresult.entity.OXMotion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OXSocketSelectionRequestDto {
    private Long memberId;
    private OXMotion answer;
}
