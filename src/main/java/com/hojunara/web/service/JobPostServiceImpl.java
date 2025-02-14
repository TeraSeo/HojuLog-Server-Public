package com.hojunara.web.service;

import com.hojunara.web.aws.s3.AwsFileService;
import com.hojunara.web.dto.request.JobPostDto;
import com.hojunara.web.dto.request.UpdateJobMainInfoPostDto;
import com.hojunara.web.dto.request.UpdateJobPostDto;
import com.hojunara.web.dto.request.UpdatePropertyMainInfoPostDto;
import com.hojunara.web.entity.*;
import com.hojunara.web.exception.JobPostNotFoundException;
import com.hojunara.web.repository.JobPostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class JobPostServiceImpl implements JobPostService {

    private final JobPostRepository jobPostRepository;
    private final UserService userService;
    private final AwsFileService awsFileService;
    private final ImageService imageService;
    private final KeywordService keywordService;

    @Autowired
    public JobPostServiceImpl(JobPostRepository jobPostRepository, UserService userService, AwsFileService awsFileService, ImageService imageService, KeywordService keywordService) {
        this.jobPostRepository = jobPostRepository;
        this.userService = userService;
        this.awsFileService = awsFileService;
        this.imageService = imageService;
        this.keywordService = keywordService;
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
    public Page<JobPost> getCreatedAtDescPostsByPage(Pageable pageable) {
        try {
            Page<JobPost> posts = jobPostRepository.findAllByOrderByCreatedAtDesc(pageable);
            log.info("Successfully got pageable Job Posts order by createdAt Desc");
            return posts;
        } catch (Exception e) {
            log.error("Failed to get pageable Job Posts order by createdAt Desc", e);
            throw e;
        }
    }

    @Override
    public Page<JobPost> getCreatedAtDescPostsByPageNSubCategory(SubCategory subCategory, Pageable pageable) {
        try {
            Page<JobPost> posts = jobPostRepository.findAllBySubCategoryOrderByCreatedAtDesc(subCategory, pageable);
            log.info("Successfully got pageable Job Posts order by createdAt Desc and subcategory: {}", subCategory);
            return posts;
        } catch (Exception e) {
            log.error("Failed to get pageable Job Posts order by createdAt Desc and subcategory: {}", subCategory, e);
            throw e;
        }
    }

    @Override
    public List<JobPost> getRecent5Posts() {
        try {
            List<JobPost> posts = jobPostRepository.findTop5ByOrderByCreatedAtDesc();
            log.info("Successfully got Recent 5 Job Posts");
            return posts;
        } catch (Exception e) {
            log.error("Failed to get Recent 5 Job Posts", e);
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
                    .postType(PostType.NORMAL)
                    .contact(jobPostDto.getContact())
                    .email(jobPostDto.getEmail())
                    .jobType(jobPostDto.getJobType())
                    .location(jobPostDto.getLocation())
                    .suburb(jobPostDto.getSuburb())
                    .isCommentAllowed(jobPostDto.getIsCommentAllowed())
                    .build();

            jobPost.setUser(user);
            JobPost createdPost = jobPostRepository.save(jobPost);

            // save post images data
            if (images != null) {
                Arrays.stream(images)
                        .map(image -> awsFileService.uploadPostFile(image, user.getEmail()))
                        .forEach(imageUrl -> imageService.createImage(imageUrl, createdPost));
            }

            // save keywords
            jobPostDto.getSelectedKeywords().stream().forEach(
                    keyword -> keywordService.createKeyword(keyword, createdPost)
            );

            log.info("Successfully created job post");

            return createdPost;
        } catch (Exception e) {
            log.error("Failed to create job post", e);
            return null;
        }
    }

    @Override
    public Post updatePost(UpdateJobPostDto updateJobPostDto, MultipartFile[] images) {
        UpdateJobMainInfoPostDto updateJobMainInfoPostDto = updateJobPostDto.getUpdateJobMainInfoPostDto();
        User user = userService.getUserById(updateJobMainInfoPostDto.getUserId());
        try {
            JobPost jobPost = getPostById(updateJobMainInfoPostDto.getPostId());

            boolean isUpdated = false;

            if (!jobPost.getTitle().equals(updateJobMainInfoPostDto.getTitle())) {
                jobPost.setTitle(updateJobMainInfoPostDto.getTitle());
                isUpdated = true;
            }
            if (!Objects.equals(jobPost.getDescription(), updateJobMainInfoPostDto.getDescription())) {
                jobPost.setDescription(updateJobMainInfoPostDto.getDescription());
                isUpdated = true;
            }
            if (!Objects.equals(jobPost.getContact(), updateJobMainInfoPostDto.getContact())) {
                jobPost.setContact(updateJobMainInfoPostDto.getContact());
                isUpdated = true;
            }
            if (!Objects.equals(jobPost.getEmail(), updateJobMainInfoPostDto.getEmail())) {
                jobPost.setEmail(updateJobMainInfoPostDto.getEmail());
                isUpdated = true;
            }
            if (!Objects.equals(jobPost.getSuburb(), updateJobMainInfoPostDto.getSuburb())) {
                jobPost.setSuburb(updateJobMainInfoPostDto.getSuburb());
                isUpdated = true;
            }
            if (!Objects.equals(jobPost.getJobType(), updateJobMainInfoPostDto.getJobType())) {
                jobPost.setJobType(updateJobMainInfoPostDto.getJobType());
                isUpdated = true;
            }
            if (!Objects.equals(jobPost.getLocation(), updateJobMainInfoPostDto.getLocation())) {
                jobPost.setLocation(updateJobMainInfoPostDto.getLocation());
                isUpdated = true;
            }
            if (!jobPost.getIsCommentAllowed().equals(updateJobMainInfoPostDto.getIsCommentAllowed())) {
                jobPost.setIsCommentAllowed(updateJobMainInfoPostDto.getIsCommentAllowed());
                isUpdated = true;
            }

            List<String> imageUrls = jobPost.getImages().stream().map(Image::getUrl).collect(Collectors.toList());
            List<String> updatedExistingImageUrls = updateJobPostDto.getUpdateJobMediaInfoPostDto().getExistingImages();
            if (!imageUrls.equals(updatedExistingImageUrls)) {
                List<String> removedImageUrls = imageUrls.stream()
                        .filter(imageUrl -> !updatedExistingImageUrls.contains(imageUrl))
                        .collect(Collectors.toList());

                if (!removedImageUrls.isEmpty()) {
                    jobPost.getImages().removeIf(image -> removedImageUrls.contains(image.getUrl()));
                }
            }

            // 키워드 업데이트
            List<String> originalKeywords = jobPost.getKeywords().stream().map(Keyword::getKeyWord).collect(Collectors.toList());
            List<String> updatedKeywords = updateJobMainInfoPostDto.getSelectedKeywords();
            if (!originalKeywords.equals(updatedKeywords)) {
                List<String> addedKeywords = updatedKeywords.stream()
                        .filter(keyword -> !originalKeywords.contains(keyword))
                        .collect(Collectors.toList());

                List<String> removedKeywords = originalKeywords.stream()
                        .filter(keyword -> !updatedKeywords.contains(keyword))
                        .collect(Collectors.toList());

                if (!addedKeywords.isEmpty()) {
                    addedKeywords.forEach(keyword -> {
                        keywordService.createKeyword(keyword, jobPost);
                    });
                }

                if (!removedKeywords.isEmpty()) {
                    jobPost.getKeywords().removeIf(keyword -> removedKeywords.contains(keyword.getKeyWord()));
                }

                isUpdated = true;
            }

            if (isUpdated) {
                jobPostRepository.save(jobPost);
                jobPostRepository.flush(); // 업데이트 내용 반영
            }

            // save post images data
            if (images != null) {
                JobPost createdPost = getPostById(updateJobMainInfoPostDto.getPostId());
                Arrays.stream(images)
                        .map(image -> awsFileService.uploadPostFile(image, user.getEmail()))
                        .forEach(imageUrl -> imageService.createImage(imageUrl, createdPost));
            }

            log.info("Successfully updated job post");

            return jobPost;
        } catch (Exception e) {
            log.error("Failed to update job post", e);
            return null;
        }
    }
}
