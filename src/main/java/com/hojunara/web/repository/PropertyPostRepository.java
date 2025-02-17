package com.hojunara.web.repository;

import com.hojunara.web.entity.Category;
import com.hojunara.web.entity.PropertyPost;
import com.hojunara.web.entity.SubCategory;
import com.hojunara.web.entity.Suburb;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyPostRepository extends JpaRepository<PropertyPost, Long> {
    Page<PropertyPost> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<PropertyPost> findAllBySubCategoryOrderByCreatedAtDesc(SubCategory subCategory, Pageable pageable);

    List<PropertyPost> findTop5ByOrderByCreatedAtDesc();

    List<PropertyPost> findByTitleContainingOrderByCreatedAtDesc(String title);

    List<PropertyPost> findBySubCategoryOrderByCreatedAtDesc(SubCategory subCategory);

    List<PropertyPost> findBySuburbOrderByCreatedAtDesc(Suburb suburb);

    List<PropertyPost> findByTitleContainingAndSubCategoryOrderByCreatedAtDesc(String title, SubCategory subCategory);

    List<PropertyPost> findByTitleContainingAndSuburbOrderByCreatedAtDesc(String title, Suburb suburb);

    List<PropertyPost> findBySubCategoryAndSuburbOrderByCreatedAtDesc(SubCategory subCategory, Suburb suburb);

    List<PropertyPost> findByTitleContainingAndSubCategoryAndSuburbOrderByCreatedAtDesc(String title, SubCategory subCategory, Suburb suburb);
}
