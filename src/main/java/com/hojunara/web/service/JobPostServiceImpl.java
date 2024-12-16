package com.hojunara.web.service;

import com.hojunara.web.aws.s3.AwsFileService;
import com.hojunara.web.dto.request.JobPostDto;
import com.hojunara.web.entity.*;
import com.hojunara.web.exception.JobPostNotFoundException;
import com.hojunara.web.repository.JobPostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class JobPostServiceImpl implements JobPostService {

    private final JobPostRepository jobPostRepository;
    private final UserService userService;
    private final AwsFileService awsFileService;
    private final ImageService imageService;

    @Autowired
    public JobPostServiceImpl(JobPostRepository jobPostRepository, UserService userService, AwsFileService awsFileService, ImageService imageService) {
        this.jobPostRepository = jobPostRepository;
        this.userService = userService;
        this.awsFileService = awsFileService;
        this.imageService = imageService;
    }

    @Override
    public List<JobPost> getWholePosts() {
        try {
            List<JobPost> posts = jobPostRepository.findAll();
            log.info("Successfully got Whole Job Posts");
            return posts;
        } catch (Exception e) {
            log.error("Failed to get Whole Job Posts", e);
            throw e;
        }
    }

    @Override
    public JobPost getPostById(Long id) {
        try {
            Optional<JobPost> j = jobPostRepository.findById(id);
            if (j.isPresent()) {
                log.info("Successfully found job post with id: {}", id);
                return j.get();
            }
            throw new JobPostNotFoundException("Job Post not found with id: " + id);
        } catch (Exception e) {
            log.error("Failed to find job post with id: {}", id, e);
            throw e;
        }
    }

    @Override
    public Post createPost(JobPostDto jobPostDto, MultipartFile[] images) {
        User user = userService.getUserById(jobPostDto.getUserId());
        try {
            JobPost jobPost = JobPost.builder()
                    .title(jobPostDto.getTitle())
                    .description(jobPostDto.getDescription())
                    .category(Category.구인구직)
                    .subCategory(jobPostDto.getSubCategory())
                    .contact(jobPostDto.getContact())
                    .email(jobPostDto.getEmail())
                    .isPortrait(jobPostDto.getIsPortrait())
                    .viewCounts(0L)
                    .jobType(jobPostDto.getJobType())
                    .suburb(jobPostDto.getSuburb())
                    .build();

            jobPost.setUser(user);
            JobPost createdPost = jobPostRepository.save(jobPost);

            // save post images data
            if (images != null) {
                Arrays.stream(images)
                        .map(image -> awsFileService.uploadPostFile(image, user.getEmail()))
                        .forEach(imageUrl -> imageService.createImage(imageUrl, createdPost));
            }

            log.info("Successfully created job post");

            return createdPost;
        } catch (Exception e) {
            log.error("Failed to create job post", e);
            return null;
        }
    }
}
