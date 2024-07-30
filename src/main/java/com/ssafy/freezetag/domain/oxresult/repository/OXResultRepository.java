package com.ssafy.freezetag.domain.oxresult.repository;

import com.ssafy.freezetag.domain.oxresult.entity.OXResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OXResultRepository extends JpaRepository<OXResult, Long> {
}
