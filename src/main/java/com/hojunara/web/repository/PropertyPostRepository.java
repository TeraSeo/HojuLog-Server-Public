package com.hojunara.web.repository;

import com.hojunara.web.entity.PropertyPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyPostRepository extends JpaRepository<PropertyPost, Long> {
    List<PropertyPost> findTop5ByOrderByCreatedAtDesc();
}
