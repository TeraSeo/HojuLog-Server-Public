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
    List<TransactionPost> findAllByOrderByUpdatedAtDesc();

    List<TransactionPost> findTop5ByOrderByUpdatedAtDesc();

    Page<TransactionPost> findAllBySubCategoryOrderByUpdatedAtDesc(SubCategory subCategory, Pageable pageable);

    List<TransactionPost> findByTitleContainingOrderByUpdatedAtDesc(String title);

    List<TransactionPost> findBySubCategoryOrderByUpdatedAtDesc(SubCategory subCategory);

    List<TransactionPost> findBySuburbOrderByUpdatedAtDesc(Suburb suburb);

    List<TransactionPost> findByTitleContainingAndSubCategoryOrderByUpdatedAtDesc(String title, SubCategory subCategory);

    List<TransactionPost> findByTitleContainingAndSuburbOrderByUpdatedAtDesc(String title, Suburb suburb);

    List<TransactionPost> findBySubCategoryAndSuburbOrderByUpdatedAtDesc(SubCategory subCategory, Suburb suburb);

    List<TransactionPost> findByTitleContainingAndSubCategoryAndSuburbOrderByUpdatedAtDesc(String title, SubCategory subCategory, Suburb suburb);

    @Query("SELECT p FROM Post p WHERE p.pinnedAdExpiry < CURRENT_TIMESTAMP")
    List<TransactionPost> findExpiredPinnedAds();

    @Query("SELECT p FROM TransactionPost p " +
            "ORDER BY " +
            "CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END, " +
            "p.updatedAt DESC")
    Page<TransactionPost> findAllWithPinnedFirst(Pageable pageable);
}
