package com.hojunara.web.service;

import com.hojunara.web.dto.request.JobPostDto;
import com.hojunara.web.entity.JobPost;
import com.hojunara.web.entity.Post;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface JobPostService {
    List<JobPost> getWholePosts();

    List<JobPost> getRecent5Posts();

    JobPost getPostById(Long id);

    Post createPost(JobPostDto jobPostDto, MultipartFile[] images);
}
