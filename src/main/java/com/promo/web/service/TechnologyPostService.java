package com.promo.web.service;

import com.promo.web.dto.TechnologyPostDto;
import com.promo.web.entity.TechnologyPost;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TechnologyPostService {
    List<TechnologyPost> getWholePosts();

    TechnologyPost getPostById(Long id);

    Boolean createPost(String email, TechnologyPostDto technologyPostDto, MultipartFile logoImage, MultipartFile[] images, MultipartFile[] videos);
}
