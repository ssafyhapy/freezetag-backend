package com.ssafy.freezetag.domain.room.service.response;

import com.ssafy.freezetag.domain.introresult.service.response.IntroReportResponseDto;
import com.ssafy.freezetag.domain.oxresult.service.response.OXReportResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class RoomReportResponseDto {
    // TODO : 참여 회원 정보 (이름, 프로필 이미지 경로)

    private final List<IntroReportResponseDto> introReportResponseDtos;

    private final List<OXReportResponseDto> oxReportResponseDtos;
    
    // TODO : 밸런스 게임 결과

}
