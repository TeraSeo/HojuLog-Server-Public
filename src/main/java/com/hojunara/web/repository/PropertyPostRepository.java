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
public interface PropertyPostRepository extends JpaRepository<PropertyPost, Long> {
    List<PropertyPost> findAllByOrderByUpdatedAtDesc();

    Page<PropertyPost> findAllBySubCategoryOrderByUpdatedAtDesc(SubCategory subCategory, Pageable pageable);

    @Query("""
            SELECT p FROM PropertyPost p
            LEFT JOIN p.likes l
            WHERE p.subCategory = :subCategory
            GROUP BY p
            ORDER BY COUNT(l) DESC, p.updatedAt DESC
        """)
    Page<PropertyPost> findAllBySubCategoryOrderByLikesDesc(@Param("subCategory") SubCategory subCategory, Pageable pageable);

    Page<PropertyPost> findAllBySubCategoryOrderByViewCountsDesc(SubCategory subCategory, Pageable pageable);

    Page<PropertyPost> findAllBySubCategoryAndPriceBetweenOrderByUpdatedAtDesc(SubCategory subCategory,
                                                                               Long minPrice,
                                                                               Long maxPrice,
                                                                               Pageable pageable);

    @Query("""
            SELECT p FROM PropertyPost p
            LEFT JOIN p.likes l
            WHERE p.subCategory = :subCategory
              AND p.price BETWEEN :minPrice AND :maxPrice
            GROUP BY p
            ORDER BY COUNT(l) DESC, p.updatedAt DESC
        """)
    Page<PropertyPost> findAllBySubCategoryAndPriceBetweenOrderByLikesDesc(@Param("subCategory") SubCategory subCategory,
                                                                           @Param("minPrice") Long minPrice,
                                                                           @Param("maxPrice") Long maxPrice,
                                                                           Pageable pageable);

    Page<PropertyPost> findAllBySubCategoryAndPriceBetweenOrderByViewCountsDesc(SubCategory subCategory,
                                                                                Long minPrice,
                                                                                Long maxPrice,
                                                                                Pageable pageable);

    Page<PropertyPost> findAllBySubCategoryAndPeriodOrderByUpdatedAtDesc(SubCategory subCategory,
                                                                         Period period,
                                                                         Pageable pageable);

    @Query("""
            SELECT p FROM PropertyPost p
            LEFT JOIN p.likes l
            WHERE p.subCategory = :subCategory
              AND p.period = :period
            GROUP BY p
            ORDER BY COUNT(l) DESC, p.updatedAt DESC
        """)
    Page<PropertyPost> findAllBySubCategoryAndPeriodOrderByLikesDesc(@Param("subCategory") SubCategory subCategory,
                                                                     @Param("period") Period period,
                                                                     Pageable pageable);

    Page<PropertyPost> findAllBySubCategoryAndPeriodOrderByViewCountsDesc(SubCategory subCategory,
                                                                          Period period,
                                                                          Pageable pageable);

    Page<PropertyPost> findAllBySubCategoryAndPriceBetweenAndPeriodOrderByUpdatedAtDesc(SubCategory subCategory,
                                                                                        Long minPrice,
                                                                                        Long maxPrice,
                                                                                        Period period,
                                                                                        Pageable pageable);

    @Query("""
            SELECT p FROM PropertyPost p
            LEFT JOIN p.likes l
            WHERE p.subCategory = :subCategory
              AND p.price BETWEEN :minPrice AND :maxPrice
              AND p.period = :period
            GROUP BY p
            ORDER BY COUNT(l) DESC, p.updatedAt DESC
        """)
    Page<PropertyPost> findAllBySubCategoryAndPriceBetweenAndPeriodOrderByLikesDesc(@Param("subCategory") SubCategory subCategory,
                                                                                    @Param("minPrice") Long minPrice,
                                                                                    @Param("maxPrice") Long maxPrice,
                                                                                    @Param("period") Period period,
                                                                                    Pageable pageable);

    Page<PropertyPost> findAllBySubCategoryAndPriceBetweenAndPeriodOrderByViewCountsDesc(SubCategory subCategory,
                                                                                         Long minPrice,
                                                                                         Long maxPrice,
                                                                                         Period period,
                                                                                         Pageable pageable);

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

    @Query("""
            SELECT p FROM PropertyPost p
            LEFT JOIN p.likes l
            GROUP BY p
            ORDER BY
              CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END,
              COUNT(l) DESC,
              p.updatedAt DESC
        """)
    Page<PropertyPost> findAllWithPinnedFirstOrderByLikeCountDesc(Pageable pageable);

    @Query("""
            SELECT p FROM PropertyPost p
            ORDER BY
              CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END,
              p.viewCounts DESC,
              p.updatedAt DESC
        """)
    Page<PropertyPost> findAllWithPinnedFirstOrderByViewCountsDesc(Pageable pageable);

    @Query("SELECT p FROM PropertyPost p " +
            "WHERE p.price BETWEEN :minPrice AND :maxPrice " +
            "ORDER BY " +
            "CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END, " +
            "p.updatedAt DESC")
    Page<PropertyPost> findAllWithPinnedFirstByPriceBetween(@Param("minPrice") Long minPrice,
                                                            @Param("maxPrice") Long maxPrice,
                                                            Pageable pageable);

    @Query("""
            SELECT p FROM PropertyPost p
            LEFT JOIN p.likes l
            WHERE p.price BETWEEN :minPrice AND :maxPrice
            GROUP BY p
            ORDER BY
                CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END,
                COUNT(l) DESC,
                p.updatedAt DESC
        """)
    Page<PropertyPost> findAllWithPinnedFirstByPriceBetweenOrderByLikeCountDesc(
            @Param("minPrice") Long minPrice,
            @Param("maxPrice") Long maxPrice,
            Pageable pageable
    );

    @Query("""
            SELECT p FROM PropertyPost p
            WHERE p.price BETWEEN :minPrice AND :maxPrice
            ORDER BY
                CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END,
                p.viewCounts DESC,
                p.updatedAt DESC
        """)
    Page<PropertyPost> findAllWithPinnedFirstByPriceBetweenOrderByViewCountsDesc(
            @Param("minPrice") Long minPrice,
            @Param("maxPrice") Long maxPrice,
            Pageable pageable
    );

    @Query("SELECT p FROM PropertyPost p " +
            "WHERE p.period = :period " +
            "ORDER BY " +
            "CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END, " +
            "p.updatedAt DESC")
    Page<PropertyPost> findAllWithPinnedFirstByPeriod(@Param("period") Period period, Pageable pageable);

    @Query("""
            SELECT p FROM PropertyPost p
            LEFT JOIN p.likes l
            WHERE p.period = :period
            GROUP BY p
            ORDER BY
                CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END,
                COUNT(l) DESC,
                p.updatedAt DESC
        """)
    Page<PropertyPost> findAllWithPinnedFirstByPeriodOrderByLikeCountDesc(
            @Param("period") Period period,
            Pageable pageable
    );

    @Query("""
            SELECT p FROM PropertyPost p
            WHERE p.period = :period
            ORDER BY
                CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END,
                p.viewCounts DESC,
                p.updatedAt DESC
        """)
    Page<PropertyPost> findAllWithPinnedFirstByPeriodOrderByViewCountsDesc(
            @Param("period") Period period,
            Pageable pageable
    );

    @Query("SELECT p FROM PropertyPost p " +
            "WHERE p.price BETWEEN :minPrice AND :maxPrice AND p.period = :period " +
            "ORDER BY " +
            "CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END, " +
            "p.updatedAt DESC")
    Page<PropertyPost> findAllWithPinnedFirstByPriceBetweenAndPeriod(@Param("minPrice") Long minPrice,
                                                                     @Param("maxPrice") Long maxPrice,
                                                                     @Param("period") Period period,
                                                                     Pageable pageable);

    @Query("""
            SELECT p FROM PropertyPost p
            LEFT JOIN p.likes l
            WHERE p.price BETWEEN :minPrice AND :maxPrice
              AND p.period = :period
            GROUP BY p
            ORDER BY
              CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END,
              COUNT(l) DESC,
              p.updatedAt DESC
        """)
    Page<PropertyPost> findAllWithPinnedFirstByPriceBetweenAndPeriodOrderByLikesDesc(
            @Param("minPrice") Long minPrice,
            @Param("maxPrice") Long maxPrice,
            @Param("period") Period period,
            Pageable pageable
    );

    @Query("""
            SELECT p FROM PropertyPost p
            WHERE p.price BETWEEN :minPrice AND :maxPrice
              AND p.period = :period
            ORDER BY
              CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END,
              p.viewCounts DESC,
              p.updatedAt DESC
        """)
    Page<PropertyPost> findAllWithPinnedFirstByPriceBetweenAndPeriodOrderByViewCountsDesc(
            @Param("minPrice") Long minPrice,
            @Param("maxPrice") Long maxPrice,
            @Param("period") Period period,
            Pageable pageable
    );
}
