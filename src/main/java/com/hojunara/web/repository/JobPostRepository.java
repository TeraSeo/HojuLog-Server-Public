package com.hojunara.web.repository;

import com.hojunara.web.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobPostRepository extends JpaRepository<JobPost, Long> {
    Page<JobPost> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<JobPost> findAllBySubCategoryOrderByCreatedAtDesc(SubCategory subCategory, Pageable pageable);

    List<JobPost> findTop5ByOrderByCreatedAtDesc();

    List<JobPost> findByTitleContainingOrderByCreatedAtDesc(String title);

    List<JobPost> findBySubCategoryOrderByCreatedAtDesc(SubCategory subCategory);

    List<JobPost> findBySuburbOrderByCreatedAtDesc(Suburb suburb);

    List<JobPost> findByTitleContainingAndSubCategoryOrderByCreatedAtDesc(String title, SubCategory subCategory);

    List<JobPost> findByTitleContainingAndSuburbOrderByCreatedAtDesc(String title, Suburb suburb);

    List<JobPost> findBySubCategoryAndSuburbOrderByCreatedAtDesc(SubCategory subCategory, Suburb suburb);

    List<JobPost> findByTitleContainingAndSubCategoryAndSuburbOrderByCreatedAtDesc(String title, SubCategory subCategory, Suburb suburb);
}
