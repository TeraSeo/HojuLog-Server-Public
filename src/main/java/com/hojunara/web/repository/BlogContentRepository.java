package com.hojunara.web.repository;

import com.hojunara.web.entity.BlogContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogContentRepository extends JpaRepository<BlogContent, Long> {
}
