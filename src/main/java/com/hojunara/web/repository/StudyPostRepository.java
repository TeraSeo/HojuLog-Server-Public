package com.hojunara.web.repository;

import com.hojunara.web.entity.StudyPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyPostRepository extends JpaRepository<StudyPost, Long> {
    Page<StudyPost> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<StudyPost> findTop5ByOrderByCreatedAtDesc();
}
