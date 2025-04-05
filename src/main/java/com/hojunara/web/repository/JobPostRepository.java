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
public interface JobPostRepository extends JpaRepository<JobPost, Long> {
    List<JobPost> findAllByOrderByUpdatedAtDesc();

    Page<JobPost> findAllBySubCategoryOrderByUpdatedAtDesc(SubCategory subCategory, Pageable pageable);

    @Query("""
    SELECT p FROM JobPost p
    WHERE p.subCategory = :subCategory
    ORDER BY SIZE(p.likes) DESC
""")
    Page<JobPost> findAllBySubCategoryOrderByLikesDesc(@Param("subCategory") SubCategory subCategory, Pageable pageable);

    Page<JobPost> findAllBySubCategoryOrderByViewCountsDesc(SubCategory subCategory, Pageable pageable);

    Page<JobPost> findAllBySubCategoryAndJobTypeOrderByUpdatedAtDesc(SubCategory subCategory,
                                                                     JobType jobType,
                                                                     Pageable pageable);

    @Query("""
            SELECT p FROM JobPost p
            WHERE p.subCategory = :subCategory AND p.jobType = :jobType
            ORDER BY SIZE(p.likes) DESC
        """)
    Page<JobPost> findAllBySubCategoryAndJobTypeOrderByLikesDesc(
            @Param("subCategory") SubCategory subCategory,
            @Param("jobType") JobType jobType,
            Pageable pageable
    );

    Page<JobPost> findAllBySubCategoryAndJobTypeOrderByViewCountsDesc(
            SubCategory subCategory,
            JobType jobType,
            Pageable pageable
    );

    List<JobPost> findTop5ByOrderByUpdatedAtDesc();

    List<JobPost> findByTitleContainingOrderByUpdatedAtDesc(String title);

    List<JobPost> findBySubCategoryOrderByUpdatedAtDesc(SubCategory subCategory);

    List<JobPost> findBySuburbOrderByUpdatedAtDesc(Suburb suburb);

    List<JobPost> findByTitleContainingAndSubCategoryOrderByUpdatedAtDesc(String title, SubCategory subCategory);

    List<JobPost> findByTitleContainingAndSuburbOrderByUpdatedAtDesc(String title, Suburb suburb);

    List<JobPost> findBySubCategoryAndSuburbOrderByUpdatedAtDesc(SubCategory subCategory, Suburb suburb);

    List<JobPost> findByTitleContainingAndSubCategoryAndSuburbOrderByUpdatedAtDesc(String title, SubCategory subCategory, Suburb suburb);

    @Query("SELECT p FROM Post p WHERE p.pinnedAdExpiry < CURRENT_TIMESTAMP")
    List<JobPost> findExpiredPinnedAds();

    @Query("SELECT p FROM JobPost p " +
            "ORDER BY " +
            "CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END, " +
            "p.updatedAt DESC")
    Page<JobPost> findAllWithPinnedFirst(Pageable pageable);

    @Query("""
        SELECT p FROM JobPost p
        LEFT JOIN p.likes l
        GROUP BY p
        ORDER BY 
          CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END,
          COUNT(l) DESC
        """)
    Page<JobPost> findAllWithPinnedFirstOrderByLikesDesc(Pageable pageable);

    @Query("""
        SELECT p FROM JobPost p
        ORDER BY 
          CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END,
          p.viewCounts DESC
        """)
    Page<JobPost> findAllWithPinnedFirstOrderByViewCountsDesc(Pageable pageable);

    @Query("SELECT p FROM JobPost p " +
            "WHERE p.jobType = :jobType " +
            "ORDER BY " +
            "CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END, " +
            "p.updatedAt DESC")
    Page<JobPost> findAllWithPinnedFirstByJobType(@Param("jobType") JobType jobType, Pageable pageable);

    @Query("""
        SELECT p FROM JobPost p
        LEFT JOIN p.likes l
        WHERE p.jobType = :jobType
        GROUP BY p
        ORDER BY 
          CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END,
          COUNT(l) DESC
        """)
    Page<JobPost> findAllWithPinnedFirstByJobTypeOrderByLikesDesc(@Param("jobType") JobType jobType, Pageable pageable);

    @Query("""
        SELECT p FROM JobPost p
        WHERE p.jobType = :jobType
        ORDER BY 
          CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END,
          p.viewCounts DESC
        """)
    Page<JobPost> findAllWithPinnedFirstByJobTypeOrderByViewCountsDesc(@Param("jobType") JobType jobType, Pageable pageable);
}
