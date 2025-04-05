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
public interface SocietyPostRepository extends JpaRepository<SocietyPost, Long> {
    List<SocietyPost> findAllByOrderByUpdatedAtDesc();

    Page<SocietyPost> findAllBySubCategoryOrderByUpdatedAtDesc(SubCategory subCategory, Pageable pageable);

    @Query("""
        SELECT p FROM SocietyPost p
        LEFT JOIN p.likes l
        WHERE p.subCategory = :subCategory
        GROUP BY p
        ORDER BY COUNT(l) DESC
        """)
    Page<SocietyPost> findAllBySubCategoryOrderByLikesDesc(@Param("subCategory") SubCategory subCategory, Pageable pageable);

    @Query("""
        SELECT p FROM SocietyPost p
        WHERE p.subCategory = :subCategory
        ORDER BY p.viewCounts DESC
        """)
    Page<SocietyPost> findAllBySubCategoryOrderByViewCountsDesc(@Param("subCategory") SubCategory subCategory, Pageable pageable);

    List<SocietyPost> findTop5ByOrderByUpdatedAtDesc();

    List<SocietyPost> findByTitleContainingOrderByUpdatedAtDesc(String title);

    List<SocietyPost> findBySubCategoryOrderByUpdatedAtDesc(SubCategory subCategory);

    List<SocietyPost> findByTitleContainingAndSubCategoryOrderByUpdatedAtDesc(String title, SubCategory subCategory);

    @Query("SELECT p FROM Post p WHERE p.pinnedAdExpiry < CURRENT_TIMESTAMP")
    List<SocietyPost> findExpiredPinnedAds();

    @Query("SELECT p FROM SocietyPost p " +
            "ORDER BY " +
            "CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END, " +
            "p.updatedAt DESC")
    Page<SocietyPost> findAllWithPinnedFirst(Pageable pageable);

    @Query("""
        SELECT p FROM SocietyPost p
        LEFT JOIN p.likes l
        GROUP BY p
        ORDER BY 
            CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END,
            COUNT(l) DESC
        """)
    Page<SocietyPost> findAllWithPinnedFirstOrderByLikesDesc(Pageable pageable);

    @Query("""
        SELECT p FROM SocietyPost p
        ORDER BY 
            CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END,
            p.viewCounts DESC
        """)
    Page<SocietyPost> findAllWithPinnedFirstOrderByViewCountsDesc(Pageable pageable);
}
