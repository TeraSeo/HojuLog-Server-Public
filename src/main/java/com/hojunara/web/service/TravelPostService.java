package com.hojunara.web.service;

import com.hojunara.web.dto.request.TravelPostDto;
import com.hojunara.web.dto.request.UpdateTravelPostDto;
import com.hojunara.web.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TravelPostService {
    List<TravelPost> getWholePosts();

    Page<TravelPost> getCreatedAtDescPostsByPageNSuburb(Pageable pageable, String travelSuburb, String option);

    Page<TravelPost> getCreatedAtDescPostsByPage(Pageable pageable, String option);

    Page<TravelPost> getCreatedAtDescPostsByPageNSubCategory(SubCategory subCategory, Pageable pageable, String option);

    Page<TravelPost> getCreatedAtDescPostsByPageNSubCategoryNSuburb(SubCategory subCategory, Pageable pageable, String travelSuburb, String option);

    List<TravelPost> getRecent5Posts();

    TravelPost getPostById(Long id);

    Post createPost(TravelPostDto travelPostDto, MultipartFile[] images);

    Post updatePost(UpdateTravelPostDto updateTravelPostDto, MultipartFile[] images);

    List<TravelPost> searchTravelPost(String title, String subCategory, List<String> keywords);

    List<TravelPost> searchByCategory();

    List<TravelPost> searchByTitle(String title);

    List<TravelPost> searchBySubCategory(SubCategory subCategory);

    List<TravelPost> searchByTitleAndSubCategory(String title, SubCategory subCategory);

    Page<TravelPost> convertPostsAsPage(List<TravelPost> posts, Pageable pageable);
}
