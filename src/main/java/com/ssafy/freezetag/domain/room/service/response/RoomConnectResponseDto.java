package com.ssafy.freezetag.domain.room.service.response;

import com.ssafy.freezetag.domain.message.entity.MessageRedis;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoomConnectResponseDto {
    private Long roomId;
    private String roomCode;
    private String roomName;
    private Integer roomPersonCount;
    private Long hostId;
    private OpenviduResponseDto webrtc;
    private List<MessageRedis> messages;
}
