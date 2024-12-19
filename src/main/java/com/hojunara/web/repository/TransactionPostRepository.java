package com.hojunara.web.repository;

import com.hojunara.web.entity.TransactionPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionPostRepository extends JpaRepository<TransactionPost, Long> {
    List<TransactionPost> findTop5ByOrderByCreatedAtDesc();
}
