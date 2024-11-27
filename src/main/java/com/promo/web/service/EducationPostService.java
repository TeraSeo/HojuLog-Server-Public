package com.promo.web.service;

import com.promo.web.dto.EducationPostDto;
import com.promo.web.entity.EducationPost;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EducationPostService {
    List<EducationPost> getWholePosts();

    EducationPost getPostById(Long id);

    Boolean createPost(String email, EducationPostDto educationPostDto, MultipartFile logoImage, MultipartFile[] images, MultipartFile[] videos);
}
