package com.hojunara.web.repository;

import com.hojunara.web.entity.JobPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobPostRepository extends JpaRepository<JobPost, Long> {
    Page<JobPost> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<JobPost> findTop5ByOrderByCreatedAtDesc();
}
