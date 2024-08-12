package com.ssafy.freezetag.domain.room.service.helper;

import com.ssafy.freezetag.domain.balanceresult.entity.BalanceQuestion;
import com.ssafy.freezetag.domain.balanceresult.entity.BalanceResult;
import com.ssafy.freezetag.domain.balanceresult.service.BalanceResultService;
import com.ssafy.freezetag.domain.balanceresult.service.response.BalanceReportResponseDto;
import com.ssafy.freezetag.domain.balanceresult.service.response.BalanceResultResponseDto;
import com.ssafy.freezetag.domain.introresult.entity.IntroResult;
import com.ssafy.freezetag.domain.introresult.service.IntroResultService;
import com.ssafy.freezetag.domain.introresult.service.response.IntroReportResponseDto;
import com.ssafy.freezetag.domain.member.service.response.MemberReportResponseDto;
import com.ssafy.freezetag.domain.oxresult.entity.OXResult;
import com.ssafy.freezetag.domain.oxresult.service.OXResultService;
import com.ssafy.freezetag.domain.oxresult.service.response.OXReportResponseDto;
import com.ssafy.freezetag.domain.oxresult.service.response.OXResponseDto;
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
                room.getHost().getMember().getId(),
                webrtcDto
        );
    }

    public static List<MemberReportResponseDto> getMemberReportResponseDtos(List<MemberRoom> memberRooms) {
        return memberRooms.stream()
                .map(memberRoom -> {
                    return new MemberReportResponseDto(memberRoom.getMember().getId(),
                            memberRoom.getMember().getMemberName(),
                            memberRoom.getMember().getMemberProfileImageUrl());
                }).toList();
    }

    public static List<IntroReportResponseDto> getIntroReportResponseDtos(List<MemberRoom> memberRooms, IntroResultService introResultService) {
        return memberRooms.stream()
                .map(memberRoom -> {
                    String memberName = memberRoom.getMember().getMemberName();
                    IntroResult introResult = introResultService.getIntroResult(memberRoom.getId());
                    return new IntroReportResponseDto(memberName, introResult.getContent());
                }).toList();
    }

    public static List<OXReportResponseDto> getOxReportResponseDtos(List<MemberRoom> memberRooms, OXResultService oxResultService) {
        return memberRooms.stream()
                .map(memberRoom -> {
                    String memberName = memberRoom.getMember().getMemberName();
                    List<OXResult> oxResults = oxResultService.getOXResult(memberRoom.getId());

                    List<OXResponseDto> oxResponseDtos = oxResults.stream()
                            .map(oxResult -> new OXResponseDto(oxResult.getOxResultContent(), oxResult.getOxResultAnswer()))
                            .toList();

                    return new OXReportResponseDto(memberName, oxResponseDtos);
                }).toList();
    }

    public static List<BalanceReportResponseDto> getBalanceReportResponseDtos(List<BalanceQuestion> balanceQuestions, BalanceResultService balanceResultService) {
        return balanceQuestions.stream()
                .map(balanceQuestion -> {
                    List<BalanceResult> balanceResults = balanceResultService.getBalanceResult(balanceQuestion.getId());

                    List<BalanceResultResponseDto> balanceResultResponseDtos = balanceResults.stream()
                            .map(balanceResult -> new BalanceResultResponseDto(balanceResult.getMember().getMemberName(),
                                    balanceResult.getBalanceResultSelectedOption())).toList();

                    return new BalanceReportResponseDto(balanceQuestion.getBalanceQuestionOptionFirst(),
                            balanceQuestion.getBalanceQuestionOptionSecond(),
                            balanceResultResponseDtos);
                }).toList();
    }
}
