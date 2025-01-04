package com.hojunara.web.repository;

import com.hojunara.web.entity.TravelPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelPostRepository extends JpaRepository<TravelPost, Long> {
    Page<TravelPost> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<TravelPost> findTop5ByOrderByCreatedAtDesc();
}
