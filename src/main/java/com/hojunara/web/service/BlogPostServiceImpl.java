package com.hojunara.web.service;

import com.hojunara.web.entity.BlogPost;
import com.hojunara.web.exception.EntityNotFoundException;
import com.hojunara.web.repository.BlogPostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Implementation of the {@link BlogPostService} interface for managing {@link BlogPost} entities.
 * <p>
 * Provides a method for retrieving a {@link BlogPost} by its ID.
 * </p>
 *
 * @author Taejun Seo
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class BlogPostServiceImpl implements BlogPostService {

    private final BlogPostRepository blogPostRepository;

    @Autowired
    public BlogPostServiceImpl(BlogPostRepository blogPostRepository) {
        this.blogPostRepository = blogPostRepository;
    }

    /**
     * Retrieves a {@link BlogPost} by its ID.
     *
     * @param postId the ID of the {@link BlogPost} to retrieve
     * @return the {@link BlogPost} associated with the given ID
     * @throws EntityNotFoundException if no {@link BlogPost} is found with the provided ID
     */
    @Override
    public BlogPost getBlogPostById(Long postId) {
        return blogPostRepository.findById(postId)
            .orElseThrow(() -> new EntityNotFoundException("Blog Post Not Found Exception", postId));
    }
}
