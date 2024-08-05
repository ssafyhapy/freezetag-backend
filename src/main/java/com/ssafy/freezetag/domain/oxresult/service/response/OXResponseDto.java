package com.ssafy.freezetag.domain.oxresult.service.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OXResponseDto {
    private  String id;

    private  String content;

    private  boolean answer;
}
