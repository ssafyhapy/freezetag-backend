package com.ssafy.freezetag.domain.member.service.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberMemoryboxDto {
    private Long roomId;
    private String memberMemoryBoxName;
    private LocalDate memberMemoryBoxDate;
    private String memberMemoryImageUrl;
}