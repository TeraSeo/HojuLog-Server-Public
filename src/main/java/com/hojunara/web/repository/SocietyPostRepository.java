package com.hojunara.web.repository;

import com.hojunara.web.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
