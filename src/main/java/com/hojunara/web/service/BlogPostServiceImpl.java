package com.hojunara.web.service;

import com.hojunara.web.entity.BlogPost;
import com.hojunara.web.exception.BlogPostNotFoundException;
import com.hojunara.web.repository.BlogPostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class BlogPostServiceImpl implements BlogPostService {

    private final BlogPostRepository blogPostRepository;

    @Autowired
    public BlogPostServiceImpl(BlogPostRepository blogPostRepository) {
        this.blogPostRepository = blogPostRepository;
    }

    @Override
    public BlogPost getBlogPostById(Long id) {
        try {
            Optional<BlogPost> blogPost = blogPostRepository.findById(id);
            if (blogPost.isPresent()) {
                log.info("Successfully got blog post by id: {}", id);
                return blogPost.get();
            }
            throw new BlogPostNotFoundException("BlogPost not found with id: " + id);
        } catch (Exception e) {
            log.error("Failed to get blog post by id: {}", id, e);
            throw e;
        }
    }
}
