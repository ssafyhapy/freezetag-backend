package com.ssafy.freezetag.domain.room.service.helper;

import com.ssafy.freezetag.domain.room.entity.MemberRoom;
import com.ssafy.freezetag.domain.room.service.response.RoomMemberInfoDto;

import java.util.List;

public class RoomConverter {
    public static List<RoomMemberInfoDto> convertToMemberInfoDtos(List<MemberRoom> memberRooms) {
        return memberRooms.stream()
                .map(memberRoom -> new RoomMemberInfoDto(memberRoom.getMember().getId(), memberRoom.getMember().getMemberName()))
                .toList();
    }
}
