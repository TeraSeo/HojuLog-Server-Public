package com.hojunara.web.service;

import com.hojunara.web.entity.BlogPost;
import com.hojunara.web.entity.ImageContent;
import com.hojunara.web.repository.ImageContentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the {@link BlogImageContentService} interface for managing {@link ImageContent} associated with a {@link BlogPost}.
 * <p>
 * Provides functionality for creating and saving {@link ImageContent} and associating it with the given {@link BlogPost}.
 * </p>
 *
 * @author Taejun Seo
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class BlogImageContentServiceImpl implements BlogImageContentService {

    private final ImageContentRepository imageContentRepository;

    @Autowired
    public BlogImageContentServiceImpl(ImageContentRepository imageContentRepository) {
        this.imageContentRepository = imageContentRepository;
    }

    /**
     * Creates a new {@link ImageContent} and associates it with the provided {@link BlogPost}.
     *
     * @param imageContent the {@link ImageContent} to be created
     * @param blogPost the {@link BlogPost} to associate the {@link ImageContent} with
     */
    @Override
    public void createBlogImageContent(ImageContent imageContent, BlogPost blogPost) {
        imageContent.setPost(blogPost);
        blogPost.getBlogContents().add(imageContent);
        imageContentRepository.save(imageContent);
        log.info("Successfully created blog image content");
    }
}
