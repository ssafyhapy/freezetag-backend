package com.ssafy.freezetag.domain.room.service.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RoomUserJoinEvent {
    private final String event;
    private final Long roomId;
    private final RoomMemberInfoResponseDto newMemberDto;
}
