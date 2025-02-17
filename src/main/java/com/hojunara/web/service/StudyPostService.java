package com.hojunara.web.service;

import com.hojunara.web.dto.request.StudyPostDto;
import com.hojunara.web.dto.request.UpdateStudyPostDto;
import com.hojunara.web.dto.request.UpdateTravelPostDto;
import com.hojunara.web.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StudyPostService {
    List<StudyPost> getWholePosts();

    Page<StudyPost> getCreatedAtDescPostsByPage(Pageable pageable);

    Page<StudyPost> getCreatedAtDescPostsByPageNSubCategory(SubCategory subCategory, Pageable pageable);

    List<StudyPost> getRecent5Posts();

    StudyPost getPostById(Long id);

    Post createPost(StudyPostDto studyPostDto, MultipartFile[] images);

    Post updatePost(UpdateStudyPostDto updateStudyPostDto, MultipartFile[] images);

    List<StudyPost> searchStudyPost(String title, String subCategory, List<String> keywords);

    List<StudyPost> searchByCategory();

    List<StudyPost> searchByTitle(String title);

    List<StudyPost> searchBySubCategory(SubCategory subCategory);

    List<StudyPost> searchByTitleAndSubCategory(String title, SubCategory subCategory);

    Page<StudyPost> convertPostsAsPage(List<StudyPost> posts, Pageable pageable);
}
