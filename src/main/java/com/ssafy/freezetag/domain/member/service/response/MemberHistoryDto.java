package com.ssafy.freezetag.domain.member.service.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberHistoryDto {
    private  Long memberHistoryId;
    private  LocalDate memberHistoryDate;
    private  String memberHistoryContent;
}