package com.ssafy.freezetag.domain.room.service.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.freezetag.domain.exception.custom.DuplicateRoomMemberException;
import com.ssafy.freezetag.domain.exception.custom.RoomFullException;
import com.ssafy.freezetag.domain.room.entity.Room;
import com.ssafy.freezetag.domain.room.service.response.RoomMemberInfoResponseDto;
import com.ssafy.freezetag.domain.room.service.response.RoomUserJoinEvent;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class RoomValidationHelper {
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

    public void sendNewUserJoinEvent(Long roomId, Long memberId, String memberName, SimpMessagingTemplate messagingTemplate, ObjectMapper objectMapper) throws JsonProcessingException {
        RoomMemberInfoResponseDto newMemberInfo = new RoomMemberInfoResponseDto(memberId, memberName);
        RoomUserJoinEvent roomUserJoinEvent = new RoomUserJoinEvent("NEW_USER_JOINED", roomId, newMemberInfo);
        String joinEvent = objectMapper.writeValueAsString(roomUserJoinEvent);
        messagingTemplate.convertAndSend("/sub/room/" + roomId, joinEvent);
    }
}

