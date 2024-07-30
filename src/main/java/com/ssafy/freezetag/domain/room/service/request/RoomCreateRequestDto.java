package com.ssafy.freezetag.domain.room.service.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RoomCreateRequestDto {
    private final String roomName;
    private final Integer roomPersonCount;
}
