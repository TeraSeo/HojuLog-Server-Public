package com.hojunara.web.service;

import com.hojunara.web.entity.Post;
import com.hojunara.web.entity.Video;

public interface VideoService {
    Video getVideoById(Long id);

    void createVideo(String url, Post post);
}
