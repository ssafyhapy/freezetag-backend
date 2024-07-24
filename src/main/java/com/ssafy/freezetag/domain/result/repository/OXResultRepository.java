package com.ssafy.freezetag.domain.result.repository;

import com.ssafy.freezetag.domain.result.entity.OXResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OXResultRepository extends JpaRepository<OXResult, Long> {
}
