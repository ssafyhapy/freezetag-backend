package com.ssafy.freezetag.domain.member.service.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MypageResponseDto {
    private String memberName;
    private String memberProviderEmail;
    private String memberProfileImageUrl;
    private String memberIntroduction;
    private List<MemberHistoryDto> memberHistoryList;
    private List<MemberMemoryboxDto> memberMemoryboxList;
}
