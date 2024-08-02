package com.ssafy.freezetag.domain.introresult.service.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class IntroModifyRequestDto {
    private String content;

    public IntroModifyRequestDto(String content) {
        this.content = content;
    }
}
