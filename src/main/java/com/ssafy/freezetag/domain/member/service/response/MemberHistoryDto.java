package com.ssafy.freezetag.domain.member.service.response;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Builder;

@Getter
@Builder
public class MemberHistoryDto {
    private Long memberHistoryId;
    private LocalDate memberHistoryDate;
    private String memberHistoryContent;
}