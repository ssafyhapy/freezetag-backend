package com.ssafy.freezetag.domain.balanceresult.entity;

import com.ssafy.freezetag.domain.common.BaseEntity;
import com.ssafy.freezetag.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BalanceResult extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "balance_result_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "balance_question_id")
    private BalanceQuestion balanceQuestion;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private SelectedOption balanceResultSelectedOption;

    public BalanceResult(BalanceQuestion balanceQuestion, Member member, SelectedOption balanceResultSelectedOption) {
        this.balanceQuestion = balanceQuestion;
        this.member = member;
        this.balanceResultSelectedOption = balanceResultSelectedOption;
    }
}
