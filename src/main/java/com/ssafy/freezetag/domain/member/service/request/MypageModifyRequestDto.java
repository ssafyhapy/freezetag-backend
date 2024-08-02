package com.ssafy.freezetag.domain.member.service.request;

import com.ssafy.freezetag.domain.member.service.response.MemberHistoryDto;
import com.ssafy.freezetag.domain.member.service.response.MemberMemoryboxDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MypageModifyRequestDto {
    private String memberName;
    private String memberProviderEmail;
    private String memberProfileImageUrl;
    private String memberIntroduction;
    private List<MemberHistoryDto> memberHistoryList;
    private List<MemberMemoryboxDto> memberMemoryboxList;
    private List<Long> deletedHistoryList; // 삭제될 번호 제거
}
