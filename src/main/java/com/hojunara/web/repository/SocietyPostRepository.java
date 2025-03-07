package com.hojunara.web.repository;

import com.hojunara.web.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SocietyPostRepository extends JpaRepository<SocietyPost, Long> {
    Page<SocietyPost> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<SocietyPost> findAllBySubCategoryOrderByCreatedAtDesc(SubCategory subCategory, Pageable pageable);

    List<SocietyPost> findTop5ByOrderByCreatedAtDesc();

    List<SocietyPost> findByTitleContainingOrderByCreatedAtDesc(String title);

    List<SocietyPost> findBySubCategoryOrderByCreatedAtDesc(SubCategory subCategory);

    List<SocietyPost> findByTitleContainingAndSubCategoryOrderByCreatedAtDesc(String title, SubCategory subCategory);

    @Query("SELECT p FROM Post p WHERE p.pinnedAdExpiry < CURRENT_TIMESTAMP")
    List<SocietyPost> findExpiredPinnedAds();

    @Query("SELECT p FROM SocietyPost p " +
            "ORDER BY " +
            "CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END, " +
            "p.createdAt DESC")
    Page<SocietyPost> findAllWithPinnedFirst(Pageable pageable);
}
