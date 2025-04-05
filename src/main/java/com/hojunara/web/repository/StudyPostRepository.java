package com.hojunara.web.repository;

import com.hojunara.web.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyPostRepository extends JpaRepository<StudyPost, Long> {
    List<StudyPost> findAllByOrderByUpdatedAtDesc();

    Page<StudyPost> findAllBySubCategoryOrderByUpdatedAtDesc(SubCategory subCategory, Pageable pageable);

    @Query("""
        SELECT p FROM StudyPost p
        LEFT JOIN p.likes l
        WHERE p.subCategory = :subCategory
        GROUP BY p
        ORDER BY COUNT(l) DESC
        """)
    Page<StudyPost> findAllBySubCategoryOrderByLikesDesc(@Param("subCategory") SubCategory subCategory, Pageable pageable);

    @Query("""
        SELECT p FROM StudyPost p
        WHERE p.subCategory = :subCategory
        ORDER BY p.viewCounts DESC
        """)
    Page<StudyPost> findAllBySubCategoryOrderByViewCountsDesc(@Param("subCategory") SubCategory subCategory, Pageable pageable);

    List<StudyPost> findTop5ByOrderByUpdatedAtDesc();

    List<StudyPost> findByTitleContainingOrderByUpdatedAtDesc(String title);

    List<StudyPost> findBySubCategoryOrderByUpdatedAtDesc(SubCategory subCategory);

    List<StudyPost> findByTitleContainingAndSubCategoryOrderByUpdatedAtDesc(String title, SubCategory subCategory);

    @Query("SELECT p FROM Post p WHERE p.pinnedAdExpiry < CURRENT_TIMESTAMP")
    List<StudyPost> findExpiredPinnedAds();

    @Query("SELECT p FROM StudyPost p " +
            "ORDER BY " +
            "CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END, " +
            "p.updatedAt DESC")
    Page<StudyPost> findAllWithPinnedFirst(Pageable pageable);

    @Query("""
        SELECT p FROM StudyPost p
        LEFT JOIN p.likes l
        GROUP BY p
        ORDER BY 
          CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END,
          COUNT(l) DESC
        """)
    Page<StudyPost> findAllWithPinnedFirstOrderByLikesDesc(Pageable pageable);

    @Query("""
        SELECT p FROM StudyPost p
        ORDER BY 
          CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END,
          p.viewCounts DESC
        """)
    Page<StudyPost> findAllWithPinnedFirstOrderByViewCountsDesc(Pageable pageable);
}
