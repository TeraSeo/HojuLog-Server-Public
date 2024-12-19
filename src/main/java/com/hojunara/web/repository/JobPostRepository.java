package com.hojunara.web.repository;

import com.hojunara.web.entity.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobPostRepository extends JpaRepository<JobPost, Long> {
    List<JobPost> findTop5ByOrderByCreatedAtDesc();
}
