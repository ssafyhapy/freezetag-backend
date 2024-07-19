package com.ssafy.freezetag.domain.recommend.entity;

import com.ssafy.freezetag.domain.common.BaseEntity;
import com.ssafy.freezetag.domain.room.entity.MemberRoom;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
@Entity
public class BalanceRecommend extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "balance_recommend_id")
    private Long id;
    private String balanceGameContentFirst;
    private String balanceGameContentSecond;

}
