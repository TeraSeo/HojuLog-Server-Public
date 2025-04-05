package com.hojunara.web.service;

import com.hojunara.web.dto.request.JobPostDto;
import com.hojunara.web.dto.request.UpdateJobPostDto;
import com.hojunara.web.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface JobPostService {
    List<JobPost> getWholePosts();

    Page<JobPost> getCreatedAtDescPostsByPage(Pageable pageable, String option);

    Page<JobPost> getCreatedAtDescPostsByPageNJobType(Pageable pageable, JobType jobType, String option);

    Page<JobPost> getCreatedAtDescPostsByPageNSubCategory(SubCategory subCategory, Pageable pageable, String option);

    Page<JobPost> getCreatedAtDescPostsByPageNSubCategoryNJobType(SubCategory subCategory, JobType jobType, Pageable pageable, String option);

    List<JobPost> getRecent5Posts();

    JobPost getPostById(Long id);

    Post createPost(JobPostDto jobPostDto, MultipartFile[] images);

    Post updatePost(UpdateJobPostDto updateJobPostDto, MultipartFile[] images);

    List<JobPost> searchJobPost(String title, String subCategory, String suburb, List<String> keywords);

    List<JobPost> searchByCategory();

    List<JobPost> searchByTitle(String title);

    List<JobPost> searchBySubCategory(SubCategory subCategory);

    List<JobPost> searchBySuburb(Suburb suburb);

    List<JobPost> searchByTitleAndSubCategory(String title, SubCategory subCategory);

    List<JobPost> searchByTitleAndSuburb(String title, Suburb suburb);

    List<JobPost> searchBySubCategoryAndSuburb(SubCategory subCategory, Suburb suburb);

    List<JobPost> searchByTitleAndSubCategoryAndSuburb(String title, SubCategory subCategory, Suburb suburb);

    Page<JobPost> convertPostsAsPage(List<JobPost> posts, Pageable pageable);
}
