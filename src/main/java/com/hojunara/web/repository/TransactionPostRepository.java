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
public interface TransactionPostRepository extends JpaRepository<TransactionPost, Long> {
    List<TransactionPost> findAllByOrderByUpdatedAtDesc();

    List<TransactionPost> findTop5ByOrderByUpdatedAtDesc();

    Page<TransactionPost> findAllBySubCategoryOrderByUpdatedAtDesc(SubCategory subCategory, Pageable pageable);

    @Query("""
            SELECT p FROM TransactionPost p
            LEFT JOIN p.likes l
            WHERE p.subCategory = :subCategory
            GROUP BY p
            ORDER BY COUNT(l) DESC, p.updatedAt DESC
        """)
    Page<TransactionPost> findAllBySubCategoryOrderByLikeCountDesc(
            @Param("subCategory") SubCategory subCategory,
            Pageable pageable
    );

    @Query("""
            SELECT p FROM TransactionPost p
            WHERE p.subCategory = :subCategory
            ORDER BY p.viewCounts DESC, p.updatedAt DESC
        """)
    Page<TransactionPost> findAllBySubCategoryOrderByViewCountsDesc(
            @Param("subCategory") SubCategory subCategory,
            Pageable pageable
    );

    Page<TransactionPost> findAllBySubCategoryAndTransactionTypeOrderByUpdatedAtDesc(SubCategory subCategory, TransactionType transactionType, Pageable pageable);

    @Query("""
        SELECT p FROM TransactionPost p
        LEFT JOIN p.likes l
        WHERE p.subCategory = :subCategory AND p.transactionType = :transactionType
        GROUP BY p
        ORDER BY COUNT(l) DESC, p.updatedAt DESC
    """)
    Page<TransactionPost> findAllBySubCategoryAndTransactionTypeOrderByLikeCountDesc(
            @Param("subCategory") SubCategory subCategory,
            @Param("transactionType") TransactionType transactionType,
            Pageable pageable
    );

    @Query("""
            SELECT p FROM TransactionPost p
            WHERE p.subCategory = :subCategory AND p.transactionType = :transactionType
            ORDER BY p.viewCounts DESC, p.updatedAt DESC
        """)
    Page<TransactionPost> findAllBySubCategoryAndTransactionTypeOrderByViewCountsDesc(
            @Param("subCategory") SubCategory subCategory,
            @Param("transactionType") TransactionType transactionType,
            Pageable pageable
    );

    Page<TransactionPost> findAllBySubCategoryAndPriceTypeOrderByUpdatedAtDesc(SubCategory subCategory, PriceType priceType, Pageable pageable);

    @Query("""
            SELECT p FROM TransactionPost p
            LEFT JOIN p.likes l
            WHERE p.subCategory = :subCategory AND p.priceType = :priceType
            GROUP BY p
            ORDER BY COUNT(l) DESC, p.updatedAt DESC
        """)
    Page<TransactionPost> findAllBySubCategoryAndPriceTypeOrderByLikeCountDesc(
            @Param("subCategory") SubCategory subCategory,
            @Param("priceType") PriceType priceType,
            Pageable pageable
    );

    @Query("""
            SELECT p FROM TransactionPost p
            WHERE p.subCategory = :subCategory AND p.priceType = :priceType
            ORDER BY p.viewCounts DESC, p.updatedAt DESC
        """)
    Page<TransactionPost> findAllBySubCategoryAndPriceTypeOrderByViewCountsDesc(
            @Param("subCategory") SubCategory subCategory,
            @Param("priceType") PriceType priceType,
            Pageable pageable
    );

    Page<TransactionPost> findAllBySubCategoryAndTransactionTypeAndPriceTypeOrderByUpdatedAtDesc(SubCategory subCategory, TransactionType transactionType, PriceType priceType, Pageable pageable);

    @Query("""
            SELECT p FROM TransactionPost p
            LEFT JOIN p.likes l
            WHERE p.subCategory = :subCategory
              AND p.transactionType = :transactionType
              AND p.priceType = :priceType
            GROUP BY p
            ORDER BY COUNT(l) DESC, p.updatedAt DESC
        """)
    Page<TransactionPost> findAllBySubCategoryAndTransactionTypeAndPriceTypeOrderByLikeCountDesc(
            @Param("subCategory") SubCategory subCategory,
            @Param("transactionType") TransactionType transactionType,
            @Param("priceType") PriceType priceType,
            Pageable pageable
    );

    @Query("""
            SELECT p FROM TransactionPost p
            WHERE p.subCategory = :subCategory
              AND p.transactionType = :transactionType
              AND p.priceType = :priceType
            ORDER BY p.viewCounts DESC, p.updatedAt DESC
        """)
    Page<TransactionPost> findAllBySubCategoryAndTransactionTypeAndPriceTypeOrderByViewCountsDesc(
            @Param("subCategory") SubCategory subCategory,
            @Param("transactionType") TransactionType transactionType,
            @Param("priceType") PriceType priceType,
            Pageable pageable
    );

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

    @Query("""
            SELECT p FROM TransactionPost p
            LEFT JOIN p.likes l
            GROUP BY p
            ORDER BY 
                CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END,
                COUNT(l) DESC,
                p.updatedAt DESC
        """)
    Page<TransactionPost> findAllOrderByLikeCountWithPinnedFirst(Pageable pageable);

    @Query("""
            SELECT p FROM TransactionPost p
            ORDER BY 
                CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END,
                p.viewCounts DESC,
                p.updatedAt DESC
        """)
    Page<TransactionPost> findAllOrderByViewCountsWithPinnedFirst(Pageable pageable);

    @Query("SELECT p FROM TransactionPost p " +
            "WHERE p.transactionType = :transactionType " +
            "ORDER BY " +
            "CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END, " +
            "p.updatedAt DESC")
    Page<TransactionPost> findAllWithPinnedFirstByTransactionType(@Param("transactionType") TransactionType transactionType, Pageable pageable);

    @Query("""
            SELECT p FROM TransactionPost p
            LEFT JOIN p.likes l
            WHERE p.transactionType = :transactionType
            GROUP BY p
            ORDER BY 
                CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END,
                COUNT(l) DESC,
                p.updatedAt DESC
        """)
    Page<TransactionPost> findAllOrderByLikeCountWithPinnedFirstByTransactionType(
            @Param("transactionType") TransactionType transactionType,
            Pageable pageable
    );

    @Query("""
        SELECT p FROM TransactionPost p
        WHERE p.transactionType = :transactionType
        ORDER BY 
            CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END,
            p.viewCounts DESC,
            p.updatedAt DESC
    """)
    Page<TransactionPost> findAllOrderByViewCountsWithPinnedFirstByTransactionType(
            @Param("transactionType") TransactionType transactionType,
            Pageable pageable
    );

    @Query("SELECT p FROM TransactionPost p " +
            "WHERE p.priceType = :priceType " +
            "ORDER BY " +
            "CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END, " +
            "p.updatedAt DESC")
    Page<TransactionPost> findAllWithPinnedFirstByPriceType(@Param("priceType") PriceType priceType, Pageable pageable);

    @Query("""
            SELECT p FROM TransactionPost p
            LEFT JOIN p.likes l
            WHERE p.priceType = :priceType
            GROUP BY p
            ORDER BY
                CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END,
                COUNT(l) DESC,
                p.updatedAt DESC
        """)
    Page<TransactionPost> findAllOrderByLikeCountWithPinnedFirstByPriceType(
            @Param("priceType") PriceType priceType,
            Pageable pageable
    );

    @Query("""
            SELECT p FROM TransactionPost p
            WHERE p.priceType = :priceType
            ORDER BY
                CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END,
                p.viewCounts DESC,
                p.updatedAt DESC
        """)
    Page<TransactionPost> findAllOrderByViewCountsWithPinnedFirstByPriceType(
            @Param("priceType") PriceType priceType,
            Pageable pageable
    );

    @Query("SELECT p FROM TransactionPost p " +
            "WHERE p.transactionType = :transactionType AND p.priceType = :priceType " +
            "ORDER BY " +
            "CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END, " +
            "p.updatedAt DESC")
    Page<TransactionPost> findAllWithPinnedFirstByTransactionTypeAndPriceType(@Param("transactionType") TransactionType transactionType,
                                                                              @Param("priceType") PriceType priceType,
                                                                              Pageable pageable);

    @Query("""
            SELECT p FROM TransactionPost p
            LEFT JOIN p.likes l
            WHERE p.transactionType = :transactionType AND p.priceType = :priceType
            GROUP BY p
            ORDER BY
                CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END,
                COUNT(l) DESC,
                p.updatedAt DESC
        """)
    Page<TransactionPost> findAllOrderByLikeCountWithPinnedFirstByTransactionTypeAndPriceType(
            @Param("transactionType") TransactionType transactionType,
            @Param("priceType") PriceType priceType,
            Pageable pageable
    );

    @Query("""
            SELECT p FROM TransactionPost p
            WHERE p.transactionType = :transactionType AND p.priceType = :priceType
            ORDER BY
                CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END,
                p.viewCounts DESC,
                p.updatedAt DESC
        """)
    Page<TransactionPost> findAllOrderByViewCountsWithPinnedFirstByTransactionTypeAndPriceType(
            @Param("transactionType") TransactionType transactionType,
            @Param("priceType") PriceType priceType,
            Pageable pageable
    );
}
