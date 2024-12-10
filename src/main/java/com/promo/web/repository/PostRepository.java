package com.promo.web.repository;

import com.promo.web.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Post> findAllByOrderByCreatedAtAsc(Pageable pageable);

//    Page<Post> findAllByOrderByLikesCountDesc(Pageable pageable);
//
//    Page<Post> findAllByOrderByLikesCountAsc(Pageable pageable);
}
