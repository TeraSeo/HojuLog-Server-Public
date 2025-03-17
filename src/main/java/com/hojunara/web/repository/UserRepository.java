package com.hojunara.web.repository;

import com.hojunara.web.entity.Post;
import com.hojunara.web.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Page<User> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query("SELECT u FROM User u ORDER BY u.likeCountThisWeek DESC")
    List<User> findTop10ByThisWeekLikedPosts(Pageable pageable);

//    @Query("SELECT u FROM User u JOIN u.thisWeekLikedPosts p WHERE p.id = :postId")
//    List<User> findAllByThisWeekLikedPostsContaining(@Param("postId") Long postId);

    @Query("SELECT u FROM User u JOIN u.paidPosts p WHERE p.id = :postId")
    List<User> findAllByPaidPostsContaining(@Param("postId") Long postId);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.likeCountThisWeek = 0")
    void resetLikeCountThisWeek();
}
