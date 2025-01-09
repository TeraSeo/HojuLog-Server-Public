package com.hojunara.web.service;

import com.hojunara.web.aws.s3.AwsFileService;
import com.hojunara.web.dto.request.StudyPostDto;
import com.hojunara.web.entity.*;
import com.hojunara.web.exception.StudyPostNotFoundException;
import com.hojunara.web.repository.StudyPostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class StudyPostServiceImpl implements StudyPostService {

    private final StudyPostRepository studyPostRepository;
    private final UserService userService;
    private final AwsFileService awsFileService;
    private final BlogDescriptionContentService blogDescriptionContentService;
    private final BlogImageContentService blogImageContentService;

    @Autowired
    public StudyPostServiceImpl(StudyPostRepository studyPostRepository, UserService userService, AwsFileService awsFileService, BlogDescriptionContentService blogDescriptionContentService, BlogImageContentService blogImageContentService) {
        this.studyPostRepository = studyPostRepository;
        this.userService = userService;
        this.awsFileService = awsFileService;
        this.blogDescriptionContentService = blogDescriptionContentService;
        this.blogImageContentService = blogImageContentService;
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
    public Page<StudyPost> getCreatedAtDescPostsByPage(Pageable pageable) {
        try {
            Page<StudyPost> posts = studyPostRepository.findAllByOrderByCreatedAtDesc(pageable);
            log.info("Successfully got pageable Study Posts order by createdAt Desc");
            return posts;
        } catch (Exception e) {
            log.error("Failed to get pageable Study Posts order by createdAt Desc", e);
            throw e;
        }
    }

    @Override
    public List<StudyPost> getRecent5Posts() {
        try {
            List<StudyPost> posts = studyPostRepository.findTop5ByOrderByCreatedAtDesc();
            log.info("Successfully got Recent 5 Study Posts");
            return posts;
        } catch (Exception e) {
            log.error("Failed to get Recent 5 Study Posts", e);
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
                    .category(Category.유학)
                    .postType(PostType.BLOG)
                    .subCategory(studyPostDto.getSubCategory())
                    .school(studyPostDto.getSchool())
                    .major(studyPostDto.getMajor())
                    .rate(studyPostDto.getRate())
                    .build();

            studyPost.setUser(user);
            StudyPost createdPost = studyPostRepository.save(studyPost);

            // save post images data
          if (studyPostDto.getBlogContents().size() > 0) {
                List<BlogContent> blogContents = BlogContent.convertMapToBlogContent(studyPostDto.getBlogContents());
                int imgCnt = 0;
                for (int i = 0; i < blogContents.size(); i++) {
                    BlogContent blogContent = blogContents.get(i);
                    if (blogContent.getType().toString().equals("image")) {
                        if (imgCnt < images.length) {
                            String imgUrl = awsFileService.uploadPostFile(images[imgCnt], user.getEmail());
                            ImageContent imageContent = (ImageContent) blogContent;
                            imageContent.setImageUrl(imgUrl);
                            blogImageContentService.createBlogImageContent(imageContent, studyPost);
                            imgCnt++;
                        }
                    }
                    else {
                        DescriptionContent descriptionContent = (DescriptionContent) blogContent;
                        blogDescriptionContentService.createBlogDescriptionContent(descriptionContent, studyPost);
                    }
                }
            }

            log.info("Successfully created study post");

            return createdPost;
        } catch (Exception e) {
            log.error("Failed to create study post", e);
            return null;
        }
    }
}