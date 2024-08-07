package com.ssafy.freezetag.domain.member.service.request;

import com.ssafy.freezetag.domain.member.entity.STATE;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberStateSocketRequestDto {
    private STATE memberState;
}
