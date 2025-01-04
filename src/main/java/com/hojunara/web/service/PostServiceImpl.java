package com.hojunara.web.service;

import com.hojunara.web.entity.*;
import com.hojunara.web.exception.PostNotFoundException;
import com.hojunara.web.exception.PostPaginationFailedException;
import com.hojunara.web.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public List<Post> getWholePosts() {
        try {
            List<Post> posts = postRepository.findAll();
            log.info("Successfully got Whole Posts");
            return posts;
        } catch (Exception e) {
            log.error("Failed to get Whole Posts", e);
            throw e;
        }
    }

    @Override
    public Page<Post> getPostsByPageNCondition(Pageable pageable, String condition) {
        try {
            switch (condition) {
                case "Latest":
                    log.info("Successfully found latest post with pagination");
                    return postRepository.findAllByOrderByCreatedAtDesc(pageable);
                case "Oldest":
                    log.info("Successfully found oldest post with pagination");
                    return postRepository.findAllByOrderByCreatedAtAsc(pageable);
//                case "Popular":
//                    log.info("Successfully found popular post with pagination");
//                    return postRepository.findAllByOrderByLikesCountDesc(pageable);
//                case "Unpopular":
//                    log.info("Successfully found unpopular post with pagination");
//                    return postRepository.findAllByOrderByLikesCountAsc(pageable);
                default:
                    throw new PostPaginationFailedException("Post not found with condition: " + condition);
            }
        } catch (Exception e) {
            log.error("Failed to find post with paging and condition: {}", condition, e);
            throw e;
        }
    }

    @Override
    public Post getPostById(Long id) {
        try {
            Optional<Post> p = postRepository.findById(id);
            if (p.isPresent()) {
                log.info("Successfully found post with id: {}", id);
                return p.get();
            }
            throw new PostNotFoundException("Post not found with id: " + id);
        } catch (Exception e) {
            log.error("Failed to find post with id: {}", id, e);
            throw e;
        }
    }

    @Override
    public void addViewCount(Long id) {
        Post post = getPostById(id);
        try {
            post.setViewCounts(post.getViewCounts() + 1);
            log.info("Successfully added view count with id: {}", id);
        } catch (Exception e) {
            log.error("Failed to add view count with id: {}", id, e);
            throw e;
        }
    }
}
