package com.hojunara.web.service;

import com.hojunara.web.aws.s3.AwsFileService;
import com.hojunara.web.dto.request.StudyPostDto;
import com.hojunara.web.entity.*;
import com.hojunara.web.exception.StudyPostNotFoundException;
import com.hojunara.web.repository.StudyPostRepository;
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
public class StudyPostServiceImpl implements StudyPostService {

    private final StudyPostRepository studyPostRepository;
    private final UserService userService;
    private final AwsFileService awsFileService;
    private final ImageService imageService;

    @Autowired
    public StudyPostServiceImpl(StudyPostRepository studyPostRepository, UserService userService, AwsFileService awsFileService, ImageService imageService) {
        this.studyPostRepository = studyPostRepository;
        this.userService = userService;
        this.awsFileService = awsFileService;
        this.imageService = imageService;
    }

    @Override
    public List<StudyPost> getWholePosts() {
        try {
            List<StudyPost> posts = studyPostRepository.findAll();
            log.info("Successfully got Whole Study Posts");
            return posts;
        } catch (Exception e) {
            log.error("Failed to get Whole Study Posts", e);
            throw e;
        }
    }

    @Override
    public StudyPost getPostById(Long id) {
        try {
            Optional<StudyPost> s = studyPostRepository.findById(id);
            if (s.isPresent()) {
                log.info("Successfully found study post with id: {}", id);
                return s.get();
            }
            throw new StudyPostNotFoundException("Study Post not found with id: " + id);
        } catch (Exception e) {
            log.error("Failed to find study post with id: {}", id, e);
            throw e;
        }
    }

    @Override
    public Post createPost(StudyPostDto studyPostDto, MultipartFile[] images) {
        User user = userService.getUserById(studyPostDto.getUserId());
        try {
            StudyPost studyPost = StudyPost.builder()
                    .title(studyPostDto.getTitle())
                    .description(studyPostDto.getDescription())
                    .category(Category.유학)
                    .subCategory(studyPostDto.getSubCategory())
                    .contact(studyPostDto.getContact())
                    .email(studyPostDto.getEmail())
                    .isPortrait(studyPostDto.getIsPortrait())
                    .viewCounts(0L)
                    .school(studyPostDto.getSchool())
                    .major(studyPostDto.getMajor())
                    .build();

            studyPost.setUser(user);
            StudyPost createdPost = studyPostRepository.save(studyPost);

            // save post images data
            if (images != null) {
                Arrays.stream(images)
                        .map(image -> awsFileService.uploadPostFile(image, user.getEmail()))
                        .forEach(imageUrl -> imageService.createImage(imageUrl, createdPost));
            }

            log.info("Successfully created study post");

            return createdPost;
        } catch (Exception e) {
            log.error("Failed to create study post", e);
            return null;
        }
    }
}