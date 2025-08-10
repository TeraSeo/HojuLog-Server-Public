package com.hojunara.web.service;

import com.hojunara.web.entity.ArticleImage;
import com.hojunara.web.entity.ArticlePost;
import com.hojunara.web.exception.EntityNotFoundException;
import com.hojunara.web.repository.ArticleImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the {@link ArticleImageService} interface for managing {@link ArticleImage} entity.
 * <p>
 * Provides methods for retrieving an article image by its ID and creating a new {@link ArticleImage}.
 * All database operations are wrapped in a transaction.
 * </p>
 *
 * @author Taejun Seo
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ArticleImageServiceImpl implements ArticleImageService {

    private final ArticleImageRepository articleImageRepository;

    @Autowired
    public ArticleImageServiceImpl(ArticleImageRepository articleImageRepository) {
        this.articleImageRepository = articleImageRepository;
    }

    /**
     * Retrieves an article image by its ID.
     *
     * @param imageId the ID of the {@link ArticleImage} to retrieve
     * @return the {@link ArticleImage} associated with the given ID
     * @throws EntityNotFoundException if no {@link ArticleImage} is found with the provided ID
     */
    @Override
    public ArticleImage getImageById(Long imageId) {
        return articleImageRepository.findById(imageId)
            .orElseThrow(() -> new EntityNotFoundException("ArticleImage Not Found Exception", imageId));
    }

    /**
     * Creates a new {@link ArticleImage} and associates it with the provided ArticlePost.
     *
     * @param url the URL of the image
     * @param post the {@link ArticlePost} that the image will be associated with
     */
    @Override
    public void createImage(String url, ArticlePost post) {
        ArticleImage img = ArticleImage.builder().url(url).post(post).build();
        post.getImages().add(img);
        articleImageRepository.save(img);
        log.info("Successfully created article image");
    }
}
