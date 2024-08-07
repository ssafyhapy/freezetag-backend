package com.ssafy.freezetag.domain.room.service.helper;

import com.ssafy.freezetag.domain.message.entity.MessageRedis;
import com.ssafy.freezetag.domain.room.entity.MemberRoom;
import com.ssafy.freezetag.domain.room.entity.Room;
import com.ssafy.freezetag.domain.room.service.response.OpenviduResponseDto;
import com.ssafy.freezetag.domain.room.service.response.RoomConnectResponseDto;
import com.ssafy.freezetag.domain.room.service.response.RoomMemberInfoResponseDto;

import java.util.List;

public class RoomConverter {
    public static List<RoomMemberInfoResponseDto> convertToMemberInfoDtos(List<MemberRoom> memberRooms) {
        return memberRooms.stream()
                .map(memberRoom -> new RoomMemberInfoResponseDto(memberRoom.getMember().getId(), memberRoom.getMember().getMemberName()))
                .toList();
    }

    public static RoomConnectResponseDto createRoomConnectResponseDto(Room room, String enterCode, String roomName, int roomPersonCount, OpenviduResponseDto webrtcDto) {
        return new RoomConnectResponseDto(
                room.getId(),
                enterCode,
                roomName,
                roomPersonCount,
                room.getHost().getId(),
                webrtcDto
        );
    }
}
