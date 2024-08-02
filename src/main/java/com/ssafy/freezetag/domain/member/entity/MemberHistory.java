package com.ssafy.freezetag.domain.member.entity;

import com.ssafy.freezetag.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.*;

@Getter
@Entity
@Setter
@NoArgsConstructor
public class MemberHistory extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "member_history_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDate memberHistoryDate;

    private String memberHistoryContent;

    public MemberHistory(Member member, LocalDate memberHistoryDate, String memberHistoryContent) {
        this.member = member;
        this.memberHistoryDate = memberHistoryDate;
        this.memberHistoryContent = memberHistoryContent;
    }
}
