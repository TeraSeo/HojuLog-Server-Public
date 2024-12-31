package com.hojunara.web.repository;

import com.hojunara.web.entity.SocietyPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SocietyPostRepository extends JpaRepository<SocietyPost, Long> {
    Page<SocietyPost> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<SocietyPost> findTop5ByOrderByCreatedAtDesc();
}
