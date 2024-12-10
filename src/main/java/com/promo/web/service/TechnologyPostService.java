package com.promo.web.service;

import com.promo.web.dto.request.TechnologyPostDto;
import com.promo.web.entity.Post;
import com.promo.web.entity.TechnologyPost;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TechnologyPostService {
    List<TechnologyPost> getWholePosts();

    TechnologyPost getPostById(Long id);

    Post createPost(String email, TechnologyPostDto technologyPostDto, MultipartFile logoImage, MultipartFile[] images);
}
