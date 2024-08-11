package com.ssafy.freezetag.domain.member.service.helper;

import com.ssafy.freezetag.domain.exception.custom.MemberNotFoundException;
import com.ssafy.freezetag.domain.member.entity.Member;
import com.ssafy.freezetag.domain.member.entity.MemberHistory;
import com.ssafy.freezetag.domain.member.service.response.MemberHistoryDto;
import com.ssafy.freezetag.domain.member.service.response.MemberMemoryboxDto;
import com.ssafy.freezetag.domain.member.service.response.MypageResponseDto;
import com.ssafy.freezetag.domain.member.service.response.ProfileResponseDto;
import com.ssafy.freezetag.domain.room.entity.MemberRoom;
import com.ssafy.freezetag.domain.room.entity.Room;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MemberConverter {
    public static List<MemberHistoryDto> convertToMemberHistoryDto(List<MemberHistory> memberHistories) {
        return memberHistories.stream()
                .map(memberHistory -> {
                    return new MemberHistoryDto(
                            memberHistory.getId(),
                            memberHistory.getMemberHistoryDate(),
                            memberHistory.getMemberHistoryContent());
                }).toList();

    }

    public static List<MemberMemoryboxDto> convertToMemberMemoryboxDto(List<MemberRoom> memberRooms) {
        return memberRooms.stream()
                .map(memberRoom -> {
                    Room room = memberRoom.getRoom();
                    return new MemberMemoryboxDto(room.getId(),
                            room.getRoomName(),
                            room.getCreatedDate(),
                            room.getRoomAfterImageUrl());
                }).toList();
    }

    public static MypageResponseDto createMypageResponseDto(Member member, List<MemberHistory> memberHistoryList, List<MemberRoom> memberRooms) {

        // MypageResponseDto 생성 및 반환
        return new MypageResponseDto(
                member.getMemberName(),
                member.getMemberVisibility(),
                member.getMemberProviderEmail(),
                member.getMemberProfileImageUrl(),
                member.getMemberIntroduction(),
                convertToMemberHistoryDto(memberHistoryList),
                convertToMemberMemoryboxDto(memberRooms)
        );
    }

    public static ProfileResponseDto createProfileResponseDto(Member member, List<MemberHistory> memberHistoryList, List<MemberRoom> memberRooms) {

        // MypageResponseDto 생성 및 반환
        return new ProfileResponseDto(
                member.getMemberName(),
                member.getMemberProviderEmail(),
                member.getMemberProfileImageUrl(),
                member.getMemberIntroduction(),
                convertToMemberHistoryDto(memberHistoryList),
                convertToMemberMemoryboxDto(memberRooms)
        );
    }
}
