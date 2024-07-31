package com.ssafy.freezetag.domain.room.service.response;

import com.ssafy.freezetag.domain.room.service.request.OpenviduResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class RoomConnectResponseDto {
    private final Long roomId;
    private final String roomCode;
    private final String roomName;
    private final Integer roomPersonCount;
    private final List<RoomMemberInfoDto> members;
    private final Long hostId;
    private final OpenviduResponseDto webrtc;
}
