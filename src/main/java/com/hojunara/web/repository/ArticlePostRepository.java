package com.hojunara.web.repository;

import com.hojunara.web.entity.ArticlePost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticlePostRepository extends JpaRepository<ArticlePost, Long> {
    Page<ArticlePost> findAllByOrderByUpdatedAtDesc(Pageable pageable);

    @Query("SELECT p FROM ArticlePost p WHERE p.user.id = :userId")
    Page<ArticlePost> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

    List<ArticlePost> findTop5ByUserIdOrderByCreatedAtDesc(Long userId);
}
