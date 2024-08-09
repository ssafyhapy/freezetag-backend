package com.ssafy.freezetag.domain.room.service.response;

import com.ssafy.freezetag.domain.balanceresult.service.response.BalanceReportResponseDto;
import com.ssafy.freezetag.domain.introresult.service.response.IntroReportResponseDto;
import com.ssafy.freezetag.domain.member.service.response.MemberReportResponseDto;
import com.ssafy.freezetag.domain.oxresult.service.response.OXReportResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoomReportResponseDto {
    private List<MemberReportResponseDto> memberReportResponseDtos;

    private List<IntroReportResponseDto> introReportResponseDtos;

    private List<OXReportResponseDto> oxReportResponseDtos;

    private List<BalanceReportResponseDto> balanceReportResponseDtos;

    private String roomBeforeImageUrl;

    private String roomAfterImageUrl;
}