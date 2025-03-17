package com.hojunara.web.repository;

import com.hojunara.web.entity.PinnablePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PinnablePostRepository extends JpaRepository<PinnablePost, Long> {
}
