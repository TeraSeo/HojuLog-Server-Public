package com.hojunara.web.service;

import com.hojunara.web.entity.Image;
import com.hojunara.web.entity.Post;

public interface ImageService {

    Image getImageById(Long id);

    void createImage(String url, Post post);
}