package com.hojunara.web.repository;

import com.hojunara.web.entity.SubCategory;
import com.hojunara.web.entity.WorldCupPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorldCupPostRepository extends JpaRepository<WorldCupPost, Long> {
    List<WorldCupPost> findAllByOrderByUpdatedAtDesc();

    List<WorldCupPost> findTop5ByOrderByUpdatedAtDesc();

    Page<WorldCupPost> findAllBySubCategoryOrderByUpdatedAtDesc(SubCategory subCategory, Pageable pageable);

    List<WorldCupPost> findByTitleContainingOrderByUpdatedAtDesc(String title);

    List<WorldCupPost> findBySubCategoryOrderByUpdatedAtDesc(SubCategory subCategory);

    List<WorldCupPost> findByTitleContainingAndSubCategoryOrderByUpdatedAtDesc(String title, SubCategory subCategory);

    @Query("SELECT p FROM WorldCupPost p " +
            "ORDER BY " +
            "CASE WHEN p.pinnedAdExpiry > CURRENT_TIMESTAMP THEN 0 ELSE 1 END, " +
            "p.updatedAt DESC")
    Page<WorldCupPost> findAllWithPinnedFirst(Pageable pageable);
}