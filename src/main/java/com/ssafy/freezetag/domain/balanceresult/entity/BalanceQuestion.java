package com.ssafy.freezetag.domain.balanceresult.entity;

import com.ssafy.freezetag.domain.room.entity.Room;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BalanceQuestion {

    @Id
    @GeneratedValue
    @Column(name = "balance_question_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @OneToMany(mappedBy = "balanceQuestion")
    private List<BalanceResult> balanceResults = new ArrayList<>();

    private String balanceQuestionOptionFirst;

    private String balanceQuestionOptionSecond;

    public BalanceQuestion(Room room, String balanceQuestionOptionFirst, String balanceQuestionOptionSecond) {
        this.room = room;
        this.balanceQuestionOptionFirst = balanceQuestionOptionFirst;
        this.balanceQuestionOptionSecond = balanceQuestionOptionSecond;
    }
}
