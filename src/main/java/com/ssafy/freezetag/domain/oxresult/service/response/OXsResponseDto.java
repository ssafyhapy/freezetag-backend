package com.ssafy.freezetag.domain.oxresult.service.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OXsResponseDto {
    private  Long memberId;

    private  List<OXResponseDto> oxResponseDtos;
}
