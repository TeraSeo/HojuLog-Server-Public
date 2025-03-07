package com.hojunara.web.repository;

import com.hojunara.web.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyPostRepository extends JpaRepository<StudyPost, Long> {
    Page<StudyPost> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<StudyPost> findAllBySubCategoryOrderByCreatedAtDesc(SubCategory subCategory, Pageable pageable);

    List<StudyPost> findTop5ByOrderByCreatedAtDesc();

    List<StudyPost> findByTitleContainingOrderByCreatedAtDesc(String title);

    List<StudyPost> findBySubCategoryOrderByCreatedAtDesc(SubCategory subCategory);

    List<StudyPost> findByTitleContainingAndSubCategoryOrderByCreatedAtDesc(String title, SubCategory subCategory);

    @Query("SELECT p FROM Post p WHERE p.pinnedAdExpiry < CURRENT_TIMESTAMP")
    List<StudyPost> findExpiredPinnedAds();

    @Query("SELECT p FROM StudyPost p " +
            "ORDER BY " +
            "CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END, " +
            "p.createdAt DESC")
    Page<StudyPost> findAllWithPinnedFirst(Pageable pageable);
}
