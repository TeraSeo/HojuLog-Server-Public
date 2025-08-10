package com.hojunara.web.service;

import com.hojunara.web.entity.BlogPost;
import com.hojunara.web.entity.DescriptionContent;

/**
 * Service interface for managing {@link DescriptionContent} associated with a {@link BlogPost}.
 * <p>
 * Provides a method for creating {@link DescriptionContent} and associating it with a {@link BlogPost}.
 * </p>
 *
 * @author Taejun Seo
 */
public interface BlogDescriptionContentService {
    void createBlogDescriptionContent(DescriptionContent descriptionContent, BlogPost blogPost);
}
