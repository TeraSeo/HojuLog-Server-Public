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
    List<PropertyPost> findAllByOrderByUpdatedAtDesc();

    Page<PropertyPost> findAllBySubCategoryOrderByUpdatedAtDesc(SubCategory subCategory, Pageable pageable);

    List<PropertyPost> findTop5ByOrderByUpdatedAtDesc();

    List<PropertyPost> findByTitleContainingOrderByUpdatedAtDesc(String title);

    List<PropertyPost> findBySubCategoryOrderByUpdatedAtDesc(SubCategory subCategory);

    List<PropertyPost> findBySuburbOrderByUpdatedAtDesc(Suburb suburb);

    List<PropertyPost> findByTitleContainingAndSubCategoryOrderByUpdatedAtDesc(String title, SubCategory subCategory);

    List<PropertyPost> findByTitleContainingAndSuburbOrderByUpdatedAtDesc(String title, Suburb suburb);

    List<PropertyPost> findBySubCategoryAndSuburbOrderByUpdatedAtDesc(SubCategory subCategory, Suburb suburb);

    List<PropertyPost> findByTitleContainingAndSubCategoryAndSuburbOrderByUpdatedAtDesc(String title, SubCategory subCategory, Suburb suburb);

    @Query("SELECT p FROM Post p WHERE p.pinnedAdExpiry < CURRENT_TIMESTAMP")
    List<PropertyPost> findExpiredPinnedAds();

    @Query("SELECT p FROM PropertyPost p " +
            "ORDER BY " +
            "CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END, " +
            "p.updatedAt DESC")
    Page<PropertyPost> findAllWithPinnedFirst(Pageable pageable);
}
