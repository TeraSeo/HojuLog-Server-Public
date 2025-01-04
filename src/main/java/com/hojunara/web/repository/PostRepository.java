package com.hojunara.web.repository;

import com.hojunara.web.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Post> findAllByOrderByCreatedAtAsc(Pageable pageable);

//    Page<Post> findAllByOrderByLikesCountDesc(Pageable pageable);
//
//    Page<Post> findAllByOrderByLikesCountAsc(Pageable pageable);
}
