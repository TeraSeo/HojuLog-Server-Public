package com.promo.web.service;

import com.promo.web.dto.request.RestaurantPostDto;
import com.promo.web.entity.Post;
import com.promo.web.entity.RestaurantPost;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RestaurantPostService {
    List<RestaurantPost> getWholePosts();

    RestaurantPost getPostById(Long id);

    Post createPost(String email, RestaurantPostDto restaurantPostDto, MultipartFile logoImage, MultipartFile[] images);
}
