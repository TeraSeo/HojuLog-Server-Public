package com.hojunara.web.service;

import com.hojunara.web.entity.BlogContent;
import com.hojunara.web.entity.BlogPost;
import com.hojunara.web.exception.BlogContentNotFoundException;
import com.hojunara.web.repository.BlogContentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class BlogContentServiceImpl implements BlogContentService {

    private final BlogContentRepository blogContentRepository;

    @Autowired
    public BlogContentServiceImpl(BlogContentRepository blogContentRepository) {
        this.blogContentRepository = blogContentRepository;
    }

    @Override
    public BlogContent getBlogContentById(Long id) {
        try {
            Optional<BlogContent> blogContent = blogContentRepository.findById(id);
            if (blogContent.isPresent()) {
                log.info("Successfully got blog content by id: {}", id);
                return blogContent.get();
            }
            throw new BlogContentNotFoundException("Blog content not found with id: " + id);
        } catch (Exception e) {
            log.error("Failed to get blog content by id: {}", id, e);
            throw e;
        }
    }

    @Override
    public void createBlogContent(BlogContent blogContent, BlogPost blogPost) {
        try {
            blogContent.setPost(blogPost);
            blogPost.getBlogContents().add(blogContent);
            blogContentRepository.save(blogContent);
            log.info("Successfully created blog content");
        } catch (Exception e) {
            log.error("Failed to create blog content");
            throw e;
        }
    }
}
