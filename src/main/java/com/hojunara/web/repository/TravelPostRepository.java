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
public interface TravelPostRepository extends JpaRepository<TravelPost, Long> {
    List<TravelPost> findAllByOrderByUpdatedAtDesc();

    Page<TravelPost> findAllBySubCategoryOrderByUpdatedAtDesc(SubCategory subCategory, Pageable pageable);

    Page<TravelPost> findAllBySubCategoryAndTravelSuburbOrderByUpdatedAtDesc(SubCategory subCategory, String travelSuburb, Pageable pageable);

    @Query("""
    SELECT p FROM TravelPost p
    LEFT JOIN p.likes l
    WHERE p.subCategory = :subCategory
    GROUP BY p
    ORDER BY COUNT(l) DESC
""")
    Page<TravelPost> findAllBySubCategoryOrderByLikesDesc(@Param("subCategory") SubCategory subCategory, Pageable pageable);

    @Query("""
    SELECT p FROM TravelPost p
    LEFT JOIN p.likes l
    WHERE p.subCategory = :subCategory AND p.travelSuburb = :travelSuburb
    GROUP BY p
    ORDER BY COUNT(l) DESC
""")
    Page<TravelPost> findAllBySubCategoryAndTravelSuburbOrderByLikesDesc(
            @Param("subCategory") SubCategory subCategory,
            @Param("travelSuburb") String travelSuburb,
            Pageable pageable
    );

    @Query("""
    SELECT p FROM TravelPost p
    WHERE p.subCategory = :subCategory
    ORDER BY p.viewCounts DESC
""")
    Page<TravelPost> findAllBySubCategoryOrderByViewCountsDesc(@Param("subCategory") SubCategory subCategory, Pageable pageable);

    @Query("""
    SELECT p FROM TravelPost p
    WHERE p.subCategory = :subCategory AND p.travelSuburb = :travelSuburb
    ORDER BY p.viewCounts DESC
""")
    Page<TravelPost> findAllBySubCategoryAndTravelSuburbOrderByViewCountsDesc(
            @Param("subCategory") SubCategory subCategory,
            @Param("travelSuburb") String travelSuburb,
            Pageable pageable
    );

    List<TravelPost> findTop5ByOrderByUpdatedAtDesc();

    List<TravelPost> findByTitleContainingOrderByUpdatedAtDesc(String title);

    List<TravelPost> findBySubCategoryOrderByUpdatedAtDesc(SubCategory subCategory);

    List<TravelPost> findByTitleContainingAndSubCategoryOrderByUpdatedAtDesc(String title, SubCategory subCategory);

    @Query("SELECT p FROM Post p WHERE p.pinnedAdExpiry < CURRENT_TIMESTAMP")
    List<TravelPost> findExpiredPinnedAds();

    @Query("SELECT p FROM TravelPost p " +
            "ORDER BY " +
            "CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END, " +
            "p.updatedAt DESC")
    Page<TravelPost> findAllWithPinnedFirst(Pageable pageable);

    @Query("SELECT p FROM TravelPost p " +
            "WHERE p.travelSuburb = :travelSuburb " +
            "ORDER BY " +
            "CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END, " +
            "p.updatedAt DESC")
    Page<TravelPost> findAllWithPinnedFirstByTravelSuburb(@Param("travelSuburb") String travelSuburb, Pageable pageable);

    @Query("""
    SELECT p FROM TravelPost p
    LEFT JOIN p.likes l
    GROUP BY p
    ORDER BY 
      CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END,
      COUNT(l) DESC
""")
    Page<TravelPost> findAllWithPinnedFirstOrderByLikes(Pageable pageable);

    @Query("""
    SELECT p FROM TravelPost p
    LEFT JOIN p.likes l
    WHERE p.travelSuburb = :travelSuburb
    GROUP BY p
    ORDER BY 
      CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END,
      COUNT(l) DESC
""")
    Page<TravelPost> findAllWithPinnedFirstByTravelSuburbOrderByLikes(@Param("travelSuburb") String travelSuburb, Pageable pageable);

    @Query("""
    SELECT p FROM TravelPost p
    ORDER BY 
      CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END,
      p.viewCounts DESC
""")
    Page<TravelPost> findAllWithPinnedFirstOrderByViews(Pageable pageable);

    @Query("""
    SELECT p FROM TravelPost p
    WHERE p.travelSuburb = :travelSuburb
    ORDER BY 
      CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END,
      p.viewCounts DESC
""")
    Page<TravelPost> findAllWithPinnedFirstByTravelSuburbOrderByViews(@Param("travelSuburb") String travelSuburb, Pageable pageable);
}
