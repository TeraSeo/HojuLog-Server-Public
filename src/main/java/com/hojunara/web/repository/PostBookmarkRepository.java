package com.hojunara.web.repository;

import com.hojunara.web.entity.PostBookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostBookmarkRepository extends JpaRepository<PostBookmark, Long> {
    Boolean existsByUserIdAndPostId(Long userId, Long postId);

    Optional<PostBookmark> findByUserIdAndPostId(Long userId, Long postId);
}
