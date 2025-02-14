package com.hojunara.web.service;

import com.hojunara.web.dto.request.JobPostDto;
import com.hojunara.web.dto.request.UpdateJobPostDto;
import com.hojunara.web.entity.JobPost;
import com.hojunara.web.entity.Post;
import com.hojunara.web.entity.SubCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface JobPostService {
    List<JobPost> getWholePosts();

    Page<JobPost> getCreatedAtDescPostsByPage(Pageable pageable);

    Page<JobPost> getCreatedAtDescPostsByPageNSubCategory(SubCategory subCategory, Pageable pageable);

    List<JobPost> getRecent5Posts();

    JobPost getPostById(Long id);

    Post createPost(JobPostDto jobPostDto, MultipartFile[] images);

    Post updatePost(UpdateJobPostDto updateJobPostDto, MultipartFile[] images);
}
