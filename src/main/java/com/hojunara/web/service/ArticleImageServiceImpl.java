package com.hojunara.web.service;

import com.hojunara.web.entity.ArticleImage;
import com.hojunara.web.entity.ArticlePost;
import com.hojunara.web.exception.ArticleImageNotFoundException;
import com.hojunara.web.repository.ArticleImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ArticleImageServiceImpl implements ArticleImageService {

    private final ArticleImageRepository articleImageRepository;

    @Autowired
    public ArticleImageServiceImpl(ArticleImageRepository articleImageRepository) {
        this.articleImageRepository = articleImageRepository;
    }

    @Override
    public ArticleImage getImageById(Long id) {
        try {
            Optional<ArticleImage> image = articleImageRepository.findById(id);
            if (image.isPresent()) {
                log.info("Successfully got article image by id: {}", id);
                return image.get();
            }
            throw new ArticleImageNotFoundException("Article Image not found with id: " + id);
        } catch (Exception e) {
            log.error("Failed to get article image by id: {}", id, e);
            throw e;
        }
    }

    @Override
    public void createImage(String url, ArticlePost post) {
        try {
            ArticleImage img = ArticleImage.builder().url(url).post(post).build();
            post.getImages().add(img);
            articleImageRepository.save(img);
            log.info("Successfully created article image");
        } catch (Exception e) {
            log.error("Failed to create article image");
            throw e;
        }
    }
}
