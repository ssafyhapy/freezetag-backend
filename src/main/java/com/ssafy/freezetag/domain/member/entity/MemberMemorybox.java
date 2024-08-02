package com.ssafy.freezetag.domain.member.entity;

import static jakarta.persistence.FetchType.LAZY;

import com.ssafy.freezetag.domain.common.BaseEntity;
import com.ssafy.freezetag.domain.room.entity.Room;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.time.LocalDate;
import lombok.Getter;

@Getter
@Entity
public class MemberMemorybox extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "member_memorybox_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    private String memberHistoryContent;
    // TODO : 후에 room_id의 BaseEntity 활용하기
    private LocalDate memberHistoryDate;

    private String thumbnailUrl;

//    private String photoUrl;



}
