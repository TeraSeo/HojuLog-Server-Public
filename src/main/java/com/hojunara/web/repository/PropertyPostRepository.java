package com.hojunara.web.repository;

import com.hojunara.web.entity.Post;
import com.hojunara.web.entity.PropertyPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyPostRepository extends JpaRepository<PropertyPost, Long> {
    Page<PropertyPost> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<PropertyPost> findTop5ByOrderByCreatedAtDesc();
}
