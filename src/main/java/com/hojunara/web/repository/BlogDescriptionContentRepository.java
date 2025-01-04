package com.hojunara.web.repository;

import com.hojunara.web.entity.DescriptionContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogDescriptionContentRepository extends JpaRepository<DescriptionContent, Long> {
}
