package com.ssafy.freezetag.domain.member.repository;

import com.ssafy.freezetag.domain.member.entity.Member;
import com.ssafy.freezetag.domain.member.entity.MemberMemorybox;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberMemoryboxRepository extends JpaRepository<MemberMemorybox, Long> {
    // member 객체의 id로 MemberMemorybox를 조회
    List<MemberMemorybox> findByMemberId(Long memberId);
}
