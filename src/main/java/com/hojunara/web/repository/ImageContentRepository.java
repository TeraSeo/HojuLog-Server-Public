package com.hojunara.web.repository;

import com.hojunara.web.entity.ImageContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageContentRepository extends JpaRepository<ImageContent, Long> {
}
