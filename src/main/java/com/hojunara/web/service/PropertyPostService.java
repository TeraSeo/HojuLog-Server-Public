package com.hojunara.web.service;

import com.hojunara.web.dto.request.PropertyPostDto;
import com.hojunara.web.entity.Post;
import com.hojunara.web.entity.PropertyPost;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PropertyPostService {
    List<PropertyPost> getWholePosts();

    PropertyPost getPostById(Long id);

    Post createPost(PropertyPostDto propertyPostDto, MultipartFile[] images);
}
