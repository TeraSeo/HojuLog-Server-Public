package com.hojunara.web.service;

import com.hojunara.web.entity.BlogPost;
import com.hojunara.web.entity.ImageContent;

/**
 * Service interface for managing {@link ImageContent} associated with a {@link BlogPost}.
 * <p>
 * Provides a method for creating {@link ImageContent} and associating it with a {@link BlogPost}.
 * </p>
 *
 * @author Taejun Seo
 */
public interface BlogImageContentService {
    void createBlogImageContent(ImageContent imageContent, BlogPost blogPost);
}
