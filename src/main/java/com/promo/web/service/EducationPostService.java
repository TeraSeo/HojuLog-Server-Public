package com.promo.web.service;

import com.promo.web.dto.request.EducationPostDto;
import com.promo.web.entity.EducationPost;
import com.promo.web.entity.Post;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EducationPostService {
    List<EducationPost> getWholePosts();

    EducationPost getPostById(Long id);

    Post createPost(String email, EducationPostDto educationPostDto, MultipartFile logoImage, MultipartFile[] images);
}
