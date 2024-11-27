package com.promo.web.service;

import com.promo.web.entity.Image;
import com.promo.web.entity.Post;

public interface ImageService {

    Image getImageById(Long id);

    void createImage(String url, Post post);
}