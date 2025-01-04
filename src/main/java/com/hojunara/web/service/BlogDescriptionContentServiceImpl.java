package com.hojunara.web.service;

import com.hojunara.web.entity.BlogPost;
import com.hojunara.web.entity.DescriptionContent;
import com.hojunara.web.repository.BlogDescriptionContentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class BlogDescriptionContentServiceImpl implements BlogDescriptionContentService {

    private final BlogDescriptionContentRepository blogDescriptionContentRepository;

    @Autowired
    public BlogDescriptionContentServiceImpl(BlogDescriptionContentRepository blogDescriptionContentRepository) {
        this.blogDescriptionContentRepository = blogDescriptionContentRepository;
    }

    @Override
    public void createBlogDescriptionContent(DescriptionContent descriptionContent, BlogPost blogPost) {
        try {
            descriptionContent.setPost(blogPost);
            blogPost.getBlogContents().add(descriptionContent);
            blogDescriptionContentRepository.save(descriptionContent);
            log.info("Successfully created blog description content");
        } catch (Exception e) {
            log.error("Failed to create blog description content");
            throw e;
        }
    }
}
