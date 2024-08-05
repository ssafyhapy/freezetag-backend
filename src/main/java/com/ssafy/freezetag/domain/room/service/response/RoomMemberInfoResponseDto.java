package com.ssafy.freezetag.domain.room.service.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RoomMemberInfoResponseDto {
    private final Long memberId;
    private final String memberName;
}
