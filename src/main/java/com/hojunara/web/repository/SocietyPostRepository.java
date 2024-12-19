package com.hojunara.web.repository;

import com.hojunara.web.entity.SocietyPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SocietyPostRepository extends JpaRepository<SocietyPost, Long> {
    List<SocietyPost> findTop5ByOrderByCreatedAtDesc();
}
