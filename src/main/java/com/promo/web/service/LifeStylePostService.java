package com.promo.web.service;

import com.promo.web.dto.LifeStylePostDto;
import com.promo.web.entity.LifeStylePost;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface LifeStylePostService {
    List<LifeStylePost> getWholePosts();

    LifeStylePost getPostById(Long id);

    Boolean createPost(String email, LifeStylePostDto lifeStylePostDto, MultipartFile logoImage, MultipartFile[] images, MultipartFile[] videos);
}
