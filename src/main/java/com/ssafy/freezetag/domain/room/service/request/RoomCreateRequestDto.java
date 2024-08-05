package com.ssafy.freezetag.domain.room.service.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoomCreateRequestDto {
    private  String roomName;
    private  Integer roomPersonCount;
}
