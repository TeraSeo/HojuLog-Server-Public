package com.hojunara.web.repository;

import com.hojunara.web.entity.TransactionPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionPostRepository extends JpaRepository<TransactionPost, Long> {
}
