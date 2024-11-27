package com.promo.web.repository;

import com.promo.web.entity.EducationPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EducationPostRepository extends JpaRepository<EducationPost, Long> {
}
