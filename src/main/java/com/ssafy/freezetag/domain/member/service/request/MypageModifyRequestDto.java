package com.ssafy.freezetag.domain.member.service.request;

import com.ssafy.freezetag.domain.member.service.response.MemberHistoryDto;
import com.ssafy.freezetag.domain.member.service.response.MemberMemoryboxDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MypageModifyRequestDto {
    private final String memberName;
    private final String memberProviderEmail;
    private final String memberProfileImageUrl;
    private final String memberIntroduction;
    private final List<MemberHistoryDto> memberHistoryList;
    private final List<MemberMemoryboxDto> memberMemoryboxList;
    private final List<Long> deletedHistoryList; // 삭제될 번호 제거
}
