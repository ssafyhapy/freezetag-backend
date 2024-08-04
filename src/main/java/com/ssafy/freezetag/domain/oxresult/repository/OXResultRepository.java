package com.ssafy.freezetag.domain.oxresult.repository;

import com.ssafy.freezetag.domain.introresult.entity.IntroResult;
import com.ssafy.freezetag.domain.oxresult.entity.OXResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OXResultRepository extends JpaRepository<OXResult, Long> {
    List<OXResult> findAllByMemberRoomId(Long memberRoomId);
}
