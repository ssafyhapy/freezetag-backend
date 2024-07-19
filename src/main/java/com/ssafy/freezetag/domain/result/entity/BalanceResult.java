package com.ssafy.freezetag.domain.result.entity;

import com.ssafy.freezetag.domain.common.BaseEntity;
import com.ssafy.freezetag.domain.room.entity.MemberRoom;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class BalanceResult extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "balance_result_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_room_id")
    private MemberRoom memberRoom;

    private String balanceResultOptionFirst;

    private String balanceResultOptionSecond;

    @Enumerated(EnumType.STRING)
    private SelectedOption selectedOption;
}
