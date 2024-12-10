package com.promo.web.service;

import com.promo.web.dto.request.LifeStylePostDto;
import com.promo.web.entity.LifeStylePost;
import com.promo.web.entity.Post;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface LifeStylePostService {
    List<LifeStylePost> getWholePosts();

    LifeStylePost getPostById(Long id);

    Post createPost(String email, LifeStylePostDto lifeStylePostDto, MultipartFile logoImage, MultipartFile[] images);
}
