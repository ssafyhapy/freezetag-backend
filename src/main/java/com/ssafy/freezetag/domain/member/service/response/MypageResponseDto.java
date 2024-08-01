package com.ssafy.freezetag.domain.member.service.response;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MypageResponseDto {
    private String memberName;
    private String memberProviderEmail;
    private String memberProfileImageUrl;
    private String memberIntroduction;
    private List<MemberHistoryDto> memberHistoryList;
    private List<MemberMemoryboxDto> memberMemoryboxList;
}
