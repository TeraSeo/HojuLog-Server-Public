package com.hojunara.web.service;

import com.hojunara.web.dto.request.SocietyPostDto;
import com.hojunara.web.dto.request.UpdateSocietyPostDto;
import com.hojunara.web.dto.request.UpdateStudyPostDto;
import com.hojunara.web.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SocietyPostService {
    List<SocietyPost> getWholePosts();

    Page<SocietyPost> getCreatedAtDescPostsByPage(Pageable pageable, String option);

    Page<SocietyPost> getCreatedAtDescPostsByPageNSubCategory(SubCategory subCategory, Pageable pageable, String option);

    List<SocietyPost> getRecent5Posts();

    SocietyPost getPostById(Long id);

    Post createPost(SocietyPostDto societyPostDto, MultipartFile[] images);

    Post updatePost(UpdateSocietyPostDto updateSocietyPostDto, MultipartFile[] images);

    List<SocietyPost> searchSocietyPost(String title, String subCategory, List<String> keywords);

    List<SocietyPost> searchByCategory();

    List<SocietyPost> searchByTitle(String title);

    List<SocietyPost> searchBySubCategory(SubCategory subCategory);

    List<SocietyPost> searchByTitleAndSubCategory(String title, SubCategory subCategory);

    Page<SocietyPost> convertPostsAsPage(List<SocietyPost> posts, Pageable pageable);
}
