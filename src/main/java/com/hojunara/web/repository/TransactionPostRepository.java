package com.hojunara.web.repository;

import com.hojunara.web.entity.TransactionPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionPostRepository extends JpaRepository<TransactionPost, Long> {
    Page<TransactionPost> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<TransactionPost> findTop5ByOrderByCreatedAtDesc();
}
