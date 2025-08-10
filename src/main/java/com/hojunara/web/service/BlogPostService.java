package com.hojunara.web.service;

import com.hojunara.web.entity.BlogPost;
import org.springframework.stereotype.Repository;

/**
 * Service interface for managing {@link BlogPost} entities.
 * <p>
 * Provides methods for retrieving a {@link BlogPost} by its ID.
 * </p>
 *
 * @author Taejun Seo
 */
@Repository
public interface BlogPostService {
    BlogPost getBlogPostById(Long id);
}
