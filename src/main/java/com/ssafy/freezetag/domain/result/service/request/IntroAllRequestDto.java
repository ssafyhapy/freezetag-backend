package com.ssafy.freezetag.domain.result.service.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class IntroAllRequestDto {
    private Long roomId;

    public IntroAllRequestDto(Long roomId) {
        this.roomId = roomId;
    }
}
