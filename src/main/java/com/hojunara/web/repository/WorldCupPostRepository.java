package com.hojunara.web.repository;

import com.hojunara.web.entity.SubCategory;
import com.hojunara.web.entity.WorldCupPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorldCupPostRepository extends JpaRepository<WorldCupPost, Long> {
    List<WorldCupPost> findAllByOrderByUpdatedAtDesc();

    List<WorldCupPost> findTop5ByOrderByUpdatedAtDesc();

    Page<WorldCupPost> findAllBySubCategoryOrderByUpdatedAtDesc(SubCategory subCategory, Pageable pageable);

    @Query("""
        SELECT p FROM WorldCupPost p
        LEFT JOIN p.likes l
        WHERE p.subCategory = :subCategory
        GROUP BY p
        ORDER BY COUNT(l) DESC
        """)
    Page<WorldCupPost> findAllBySubCategoryOrderByLikesDesc(@Param("subCategory") SubCategory subCategory, Pageable pageable);

    @Query("""
        SELECT p FROM WorldCupPost p
        WHERE p.subCategory = :subCategory
        ORDER BY p.viewCounts DESC
        """)
    Page<WorldCupPost> findAllBySubCategoryOrderByViewCountsDesc(@Param("subCategory") SubCategory subCategory, Pageable pageable);

    List<WorldCupPost> findByTitleContainingOrderByUpdatedAtDesc(String title);

    List<WorldCupPost> findBySubCategoryOrderByUpdatedAtDesc(SubCategory subCategory);

    List<WorldCupPost> findByTitleContainingAndSubCategoryOrderByUpdatedAtDesc(String title, SubCategory subCategory);

    @Query("SELECT p FROM WorldCupPost p " +
            "ORDER BY " +
            "CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END, " +
            "p.updatedAt DESC")
    Page<WorldCupPost> findAllWithPinnedFirst(Pageable pageable);

    @Query("""
        SELECT p FROM WorldCupPost p
        LEFT JOIN p.likes l
        GROUP BY p
        ORDER BY 
          CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END,
          COUNT(l) DESC
        """)
    Page<WorldCupPost> findAllOrderByLikesWithPinnedFirst(Pageable pageable);

    @Query("""
        SELECT p FROM WorldCupPost p
        ORDER BY 
          CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END,
          p.viewCounts DESC
        """)
    Page<WorldCupPost> findAllOrderByViewCountsWithPinnedFirst(Pageable pageable);

}