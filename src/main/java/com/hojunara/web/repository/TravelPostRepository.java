package com.hojunara.web.repository;

import com.hojunara.web.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelPostRepository extends JpaRepository<TravelPost, Long> {
    Page<TravelPost> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<TravelPost> findAllBySubCategoryOrderByCreatedAtDesc(SubCategory subCategory, Pageable pageable);

    List<TravelPost> findTop5ByOrderByCreatedAtDesc();

    List<TravelPost> findByTitleContainingOrderByCreatedAtDesc(String title);

    List<TravelPost> findBySubCategoryOrderByCreatedAtDesc(SubCategory subCategory);

    List<TravelPost> findByTitleContainingAndSubCategoryOrderByCreatedAtDesc(String title, SubCategory subCategory);

    @Query("SELECT p FROM Post p WHERE p.pinnedAdExpiry < CURRENT_TIMESTAMP")
    List<TravelPost> findExpiredPinnedAds();

    @Query("SELECT p FROM TravelPost p " +
            "ORDER BY " +
            "CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END, " +
            "p.createdAt DESC")
    Page<TravelPost> findAllWithPinnedFirst(Pageable pageable);
}
