package com.hojunara.web.repository;

import com.hojunara.web.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobPostRepository extends JpaRepository<JobPost, Long> {
    List<JobPost> findAllByOrderByUpdatedAtDesc();

    Page<JobPost> findAllBySubCategoryOrderByUpdatedAtDesc(SubCategory subCategory, Pageable pageable);

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
}
