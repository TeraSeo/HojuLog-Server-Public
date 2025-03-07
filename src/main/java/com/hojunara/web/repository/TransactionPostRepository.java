package com.hojunara.web.repository;

import com.hojunara.web.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionPostRepository extends JpaRepository<TransactionPost, Long> {
    Page<TransactionPost> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<TransactionPost> findTop5ByOrderByCreatedAtDesc();

    Page<TransactionPost> findAllBySubCategoryOrderByCreatedAtDesc(SubCategory subCategory, Pageable pageable);

    List<TransactionPost> findByTitleContainingOrderByCreatedAtDesc(String title);

    List<TransactionPost> findBySubCategoryOrderByCreatedAtDesc(SubCategory subCategory);

    List<TransactionPost> findBySuburbOrderByCreatedAtDesc(Suburb suburb);

    List<TransactionPost> findByTitleContainingAndSubCategoryOrderByCreatedAtDesc(String title, SubCategory subCategory);

    List<TransactionPost> findByTitleContainingAndSuburbOrderByCreatedAtDesc(String title, Suburb suburb);

    List<TransactionPost> findBySubCategoryAndSuburbOrderByCreatedAtDesc(SubCategory subCategory, Suburb suburb);

    List<TransactionPost> findByTitleContainingAndSubCategoryAndSuburbOrderByCreatedAtDesc(String title, SubCategory subCategory, Suburb suburb);

    @Query("SELECT p FROM Post p WHERE p.pinnedAdExpiry < CURRENT_TIMESTAMP")
    List<TransactionPost> findExpiredPinnedAds();

    @Query("SELECT p FROM TransactionPost p " +
            "ORDER BY " +
            "CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END, " +
            "p.createdAt DESC")
    Page<TransactionPost> findAllWithPinnedFirst(Pageable pageable);
}
