package com.hojunara.web.repository;

import com.hojunara.web.entity.ResponseComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponseCommentRepository extends JpaRepository<ResponseComment, Long> {
}
