package com.ssafy.freezetag.domain.introresult.service.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class IntroSaveRequestDto {
    private  Long roomId;

    private  String content;
}
