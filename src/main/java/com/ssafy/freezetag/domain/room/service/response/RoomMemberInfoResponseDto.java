package com.ssafy.freezetag.domain.room.service.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoomMemberInfoResponseDto {
    private  Long memberId;
    private  String memberName;
}
