package com.ssafy.freezetag.domain.oxresult.repository;

import com.ssafy.freezetag.domain.oxresult.entity.OXResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OXResultRepository extends JpaRepository<OXResult, Long> {
    List<OXResult> findAllByMemberRoomId(Long memberRoomId);
}
