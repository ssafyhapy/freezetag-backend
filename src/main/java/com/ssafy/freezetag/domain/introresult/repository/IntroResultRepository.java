package com.ssafy.freezetag.domain.introresult.repository;

import com.ssafy.freezetag.domain.introresult.entity.IntroResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IntroResultRepository extends JpaRepository<IntroResult, Long> {
    Optional<IntroResult> findByMemberRoomId(Long memberRoomId);
}
