package com.hojunara.web.service;

import com.hojunara.web.entity.BlogPost;
import com.hojunara.web.entity.DescriptionContent;
import com.hojunara.web.repository.BlogDescriptionContentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the {@link BlogDescriptionContentService} interface for managing {@link DescriptionContent} associated with a {@link BlogPost}.
 * <p>
 * Provides functionality for creating and saving {@link DescriptionContent} and associating it with the given {@link BlogPost}.
 * </p>
 *
 * @author Taejun Seo
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class BlogDescriptionContentServiceImpl implements BlogDescriptionContentService {

    private final BlogDescriptionContentRepository blogDescriptionContentRepository;

    @Autowired
    public BlogDescriptionContentServiceImpl(BlogDescriptionContentRepository blogDescriptionContentRepository) {
        this.blogDescriptionContentRepository = blogDescriptionContentRepository;
    }

    /**
     * Creates a new {@link DescriptionContent} and associates it with the provided {@link BlogPost}.
     *
     * @param descriptionContent the {@link DescriptionContent} to be created
     * @param blogPost the {@link BlogPost} to associate the {@link DescriptionContent} with
     */
    @Override
    public void createBlogDescriptionContent(DescriptionContent descriptionContent, BlogPost blogPost) {
        descriptionContent.setPost(blogPost);
        blogPost.getBlogContents().add(descriptionContent);
        blogDescriptionContentRepository.save(descriptionContent);
        log.info("Successfully created blog description content");
    }
}
