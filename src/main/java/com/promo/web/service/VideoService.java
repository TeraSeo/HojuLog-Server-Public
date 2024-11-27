package com.promo.web.service;

import com.promo.web.entity.Post;
import com.promo.web.entity.Video;

public interface VideoService {
    Video getVideoById(Long id);

    void createVideo(String url, Post post);
}
