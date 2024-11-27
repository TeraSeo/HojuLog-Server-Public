package com.promo.web.service;

import com.promo.web.dto.EntertainmentPostDto;
import com.promo.web.entity.EntertainmentPost;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EntertainmentPostService {
    List<EntertainmentPost> getWholePosts();

    EntertainmentPost getPostById(Long id);

    Boolean createPost(String email, EntertainmentPostDto entertainmentPostDto, MultipartFile logoImage, MultipartFile[] images, MultipartFile[] videos);
}