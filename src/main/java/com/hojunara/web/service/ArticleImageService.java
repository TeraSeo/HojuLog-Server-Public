package com.hojunara.web.service;

import com.hojunara.web.entity.ArticleImage;
import com.hojunara.web.entity.ArticlePost;

/**
 * Service interface for managing {@link ArticleImage} entity.
 * <p>
 * Provides methods for retrieving an image by its ID and creating a new {@link ArticleImage}.
 * </p>
 *
 * @author Taejun Seo
 */
public interface ArticleImageService {
    ArticleImage getImageById(Long imageId);

    void createImage(String url, ArticlePost post);
}
