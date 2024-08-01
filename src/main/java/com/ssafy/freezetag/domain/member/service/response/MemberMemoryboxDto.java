package com.ssafy.freezetag.domain.member.service.response;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberMemoryboxDto {
    private LocalDate memberHistoryDate;
    private String memberHistoryContent;
    private String thumbnail;
    private String photo;
}