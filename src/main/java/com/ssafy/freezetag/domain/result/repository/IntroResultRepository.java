package com.ssafy.freezetag.domain.result.repository;

import com.ssafy.freezetag.domain.result.entity.IntroResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntroResultRepository extends JpaRepository<IntroResult, Long> {
}
