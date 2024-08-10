package com.ssafy.freezetag.domain.member.service.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponseDto {
    private String memberName;
    private String memberProviderEmail;
    private String memberProfileImageUrl;
    private String memberIntroduction;
    private List<MemberHistoryDto> memberHistoryList;
    private List<MemberMemoryboxDto> memberMemoryboxList;

}
