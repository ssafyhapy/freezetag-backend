package com.ssafy.freezetag.domain.member.service.response;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberMemoryboxDto {
    private final LocalDate memberHistoryDate;
    private final String memberHistoryContent;
    private final String thumbnailUrl;
//    private final String photo;
}