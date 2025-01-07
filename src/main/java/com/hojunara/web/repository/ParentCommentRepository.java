package com.hojunara.web.repository;

import com.hojunara.web.entity.ParentComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParentCommentRepository extends JpaRepository<ParentComment, Long> {

}
