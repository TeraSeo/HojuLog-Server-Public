package com.hojunara.web.repository;

import com.hojunara.web.entity.ArticlePost;
import com.hojunara.web.entity.PinnablePost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PinnablePostRepository extends JpaRepository<PinnablePost, Long> {
    @Query("SELECT p FROM PinnablePost p WHERE p.user.id = :userId")
    Page<PinnablePost> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

    List<PinnablePost> findTop5ByUserIdOrderByCreatedAtDesc(Long userId);
}
