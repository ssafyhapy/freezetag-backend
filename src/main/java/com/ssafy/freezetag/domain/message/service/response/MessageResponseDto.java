package com.ssafy.freezetag.domain.message.service.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponseDto {
    private String memberProfileImageUrl;

    private String memberName;

    private String content;

    private LocalDateTime time;
}
