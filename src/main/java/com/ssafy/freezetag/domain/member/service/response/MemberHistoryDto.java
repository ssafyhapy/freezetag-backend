package com.ssafy.freezetag.domain.member.service.response;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberHistoryDto {
    private final Long memberHistoryId;
    private final LocalDate memberHistoryDate;
    private final String memberHistoryContent;
}