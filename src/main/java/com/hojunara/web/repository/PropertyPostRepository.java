package com.hojunara.web.repository;

import com.hojunara.web.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyPostRepository extends JpaRepository<PropertyPost, Long> {
    Page<PropertyPost> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<PropertyPost> findAllBySubCategoryOrderByCreatedAtDesc(SubCategory subCategory, Pageable pageable);

    List<PropertyPost> findTop5ByOrderByCreatedAtDesc();

    List<PropertyPost> findByTitleContainingOrderByCreatedAtDesc(String title);

    List<PropertyPost> findBySubCategoryOrderByCreatedAtDesc(SubCategory subCategory);

    List<PropertyPost> findBySuburbOrderByCreatedAtDesc(Suburb suburb);

    List<PropertyPost> findByTitleContainingAndSubCategoryOrderByCreatedAtDesc(String title, SubCategory subCategory);

    List<PropertyPost> findByTitleContainingAndSuburbOrderByCreatedAtDesc(String title, Suburb suburb);

    List<PropertyPost> findBySubCategoryAndSuburbOrderByCreatedAtDesc(SubCategory subCategory, Suburb suburb);

    List<PropertyPost> findByTitleContainingAndSubCategoryAndSuburbOrderByCreatedAtDesc(String title, SubCategory subCategory, Suburb suburb);

    @Query("SELECT p FROM Post p WHERE p.pinnedAdExpiry < CURRENT_TIMESTAMP")
    List<PropertyPost> findExpiredPinnedAds();

    @Query("SELECT p FROM PropertyPost p " +
            "ORDER BY " +
            "CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END, " +
            "p.createdAt DESC")
    Page<PropertyPost> findAllWithPinnedFirst(Pageable pageable);
}
