package com.promo.web.repository;

import com.promo.web.entity.TechnologyPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TechnologyPostRepository extends JpaRepository<TechnologyPost, Long> {
}
