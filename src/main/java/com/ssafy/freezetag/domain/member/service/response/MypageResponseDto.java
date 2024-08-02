package com.ssafy.freezetag.domain.member.service.response;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MypageResponseDto {
    private final String memberName;
    private final String memberProviderEmail;
    private final String memberProfileImageUrl;
    private final String memberIntroduction;
    private final List<MemberHistoryDto> memberHistoryList;
    private final List<MemberMemoryboxDto> memberMemoryboxList;
}
