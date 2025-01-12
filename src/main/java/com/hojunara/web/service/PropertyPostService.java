package com.hojunara.web.service;

import com.hojunara.web.dto.request.PropertyPostDto;
import com.hojunara.web.entity.Post;
import com.hojunara.web.entity.PropertyPost;
import com.hojunara.web.entity.SubCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PropertyPostService {
    List<PropertyPost> getWholePosts();

    Page<PropertyPost> getCreatedAtDescPostsByPage(Pageable pageable);

    Page<PropertyPost> getCreatedAtDescPostsByPageNSubCategory(SubCategory subCategory, Pageable pageable);

    List<PropertyPost> getRecent5Posts();

    PropertyPost getPostById(Long id);

    Post createPost(PropertyPostDto propertyPostDto, MultipartFile[] images);
}
