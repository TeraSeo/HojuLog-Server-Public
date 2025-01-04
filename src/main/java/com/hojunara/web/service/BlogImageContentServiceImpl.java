package com.hojunara.web.service;

import com.hojunara.web.entity.BlogPost;
import com.hojunara.web.entity.ImageContent;
import com.hojunara.web.repository.ImageContentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class BlogImageContentServiceImpl implements BlogImageContentService {

    private final ImageContentRepository imageContentRepository;

    @Autowired
    public BlogImageContentServiceImpl(ImageContentRepository imageContentRepository) {
        this.imageContentRepository = imageContentRepository;
    }

    @Override
    public void createBlogImageContent(ImageContent imageContent, BlogPost blogPost) {
        try {
            imageContent.setPost(blogPost);
            blogPost.getBlogContents().add(imageContent);
            imageContentRepository.save(imageContent);
            log.info("Successfully created blog image content");
        } catch (Exception e) {
            log.error("Failed to create blog image content");
            throw e;
        }
    }
}
