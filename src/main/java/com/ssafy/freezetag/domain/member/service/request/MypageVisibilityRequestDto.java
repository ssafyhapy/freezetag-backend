package com.ssafy.freezetag.domain.member.service.request;

import com.ssafy.freezetag.domain.member.entity.Visibility;
import lombok.Data;
import lombok.Getter;

@Getter
public class MypageVisibilityRequestDto {

    Visibility visibility;
}
