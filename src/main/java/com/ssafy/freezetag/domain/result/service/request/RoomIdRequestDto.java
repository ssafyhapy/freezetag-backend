package com.ssafy.freezetag.domain.result.service.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoomIdRequestDto {
    private Long roomId;

    public RoomIdRequestDto(Long roomId) {
        this.roomId = roomId;
    }
}
