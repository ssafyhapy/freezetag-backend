package com.ssafy.freezetag.domain.member.repository;

import com.ssafy.freezetag.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMemberProviderEmail(String memberProviderEmail);
}