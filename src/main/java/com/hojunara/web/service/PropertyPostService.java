package com.hojunara.web.service;

import com.hojunara.web.dto.request.PropertyPostDto;
import com.hojunara.web.dto.request.UpdatePropertyPostDto;
import com.hojunara.web.entity.*;
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

    Post updatePost(UpdatePropertyPostDto updatePropertyPostDto, MultipartFile[] images);

    List<PropertyPost> searchPropertyPost(String title, String subCategory, String suburb, List<String> keywords);

    List<PropertyPost> searchByCategory();

    List<PropertyPost> searchByTitle(String title);

    List<PropertyPost> searchBySubCategory(SubCategory subCategory);

    List<PropertyPost> searchBySuburb(Suburb suburb);

    List<PropertyPost> searchByTitleAndSubCategory(String title, SubCategory subCategory);

    List<PropertyPost> searchByTitleAndSuburb(String title, Suburb suburb);

    List<PropertyPost> searchBySubCategoryAndSuburb(SubCategory subCategory, Suburb suburb);

    List<PropertyPost> searchByTitleAndSubCategoryAndSuburb(String title, SubCategory subCategory, Suburb suburb);

    Page<PropertyPost> convertPostsAsPage(List<PropertyPost> posts, Pageable pageable);
}
