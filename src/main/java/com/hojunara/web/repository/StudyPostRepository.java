package com.hojunara.web.repository;

import com.hojunara.web.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyPostRepository extends JpaRepository<StudyPost, Long> {
    Page<StudyPost> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<StudyPost> findAllBySubCategoryOrderByCreatedAtDesc(SubCategory subCategory, Pageable pageable);

    List<StudyPost> findTop5ByOrderByCreatedAtDesc();

    List<StudyPost> findByTitleContainingOrderByCreatedAtDesc(String title);

    List<StudyPost> findBySubCategoryOrderByCreatedAtDesc(SubCategory subCategory);

    List<StudyPost> findByTitleContainingAndSubCategoryOrderByCreatedAtDesc(String title, SubCategory subCategory);
}
