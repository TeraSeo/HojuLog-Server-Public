package com.promo.web.service;

import com.promo.web.dto.request.EntertainmentPostDto;
import com.promo.web.entity.EntertainmentPost;
import com.promo.web.entity.Post;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EntertainmentPostService {
    List<EntertainmentPost> getWholePosts();

    EntertainmentPost getPostById(Long id);

    Post createPost(String email, EntertainmentPostDto entertainmentPostDto, MultipartFile logoImage, MultipartFile[] images);
}