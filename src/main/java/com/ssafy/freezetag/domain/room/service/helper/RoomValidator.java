package com.ssafy.freezetag.domain.room.service.helper;

import com.ssafy.freezetag.domain.exception.custom.DuplicateRoomMemberException;
import com.ssafy.freezetag.domain.exception.custom.RoomFullException;
import com.ssafy.freezetag.domain.room.entity.Room;

public class RoomValidator {
    public static void validateRoomCapacity(Room room) {
        if (room.getMemberRooms().size() >= room.getRoomPersonCount()) {
            throw new RoomFullException();
        }
    }

    public static void validateDuplicateMember(Room room, Long memberId) {
        if (room.getMemberRooms().stream().anyMatch(memberRoom -> memberRoom.getMember().getId().equals(memberId))) {
            throw new DuplicateRoomMemberException();
        }
    }

    public static void validateMaxRoomPersonCount(Integer personCount) {
        if (personCount > 6) {
            throw new IllegalArgumentException("방 최대 인원은 6명입니다.");
        }
    }
}

