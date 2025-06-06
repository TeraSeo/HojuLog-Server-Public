package com.hojunara.web.repository;

import com.hojunara.web.entity.Post;
import com.hojunara.web.entity.SubCategory;
import com.hojunara.web.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findAllByOrderByUpdatedAtDesc(Pageable pageable);

    Page<Post> findAllByOrderByUpdatedAtAsc(Pageable pageable);

    @Modifying
    @Query("UPDATE Post p SET p.viewCounts = p.viewCounts + 1 WHERE p.id = :postId")
    void incrementViewCount(@Param("postId") Long postId);

//    @Query("SELECT COUNT(u) FROM Post p JOIN p.likedByUsersThisWeek u WHERE p.user.id = :userId")
//    Long countLikesThisWeekByUserId(@Param("userId") Long userId);

//    Page<Post> findAllByOrderByLikesCountDesc(Pageable pageable);
//
//    Page<Post> findAllByOrderByLikesCountAsc(Pageable pageable);
}
