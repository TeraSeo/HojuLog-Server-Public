package com.hojunara.web.service;

import com.hojunara.web.entity.ArticleImage;
import com.hojunara.web.entity.ArticlePost;

public interface ArticleImageService {
    ArticleImage getImageById(Long id);

    void createImage(String url, ArticlePost post);
}
