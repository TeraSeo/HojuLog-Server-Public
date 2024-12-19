package com.hojunara.web.repository;

import com.hojunara.web.entity.StudyPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyPostRepository extends JpaRepository<StudyPost, Long> {
    List<StudyPost> findTop5ByOrderByCreatedAtDesc();
}
