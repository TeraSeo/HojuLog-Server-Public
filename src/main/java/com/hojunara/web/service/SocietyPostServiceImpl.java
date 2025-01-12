package com.hojunara.web.service;

import com.hojunara.web.aws.s3.AwsFileService;
import com.hojunara.web.dto.request.SocietyPostDto;
import com.hojunara.web.entity.*;
import com.hojunara.web.exception.SocietyPostNotFoundException;
import com.hojunara.web.repository.SocietyPostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class SocietyPostServiceImpl implements SocietyPostService {

    private final SocietyPostRepository societyPostRepository;
    private final UserService userService;
    private final AwsFileService awsFileService;
    private final ImageService imageService;

    @Autowired
    public SocietyPostServiceImpl(SocietyPostRepository societyPostRepository, UserService userService, AwsFileService awsFileService, ImageService imageService) {
        this.societyPostRepository = societyPostRepository;
        this.userService = userService;
        this.awsFileService = awsFileService;
        this.imageService = imageService;
    }

    @Override
    public List<SocietyPost> getWholePosts() {
        try {
            List<SocietyPost> posts = societyPostRepository.findAll();
            log.info("Successfully got Whole Society Posts");
            return posts;
        } catch (Exception e) {
            log.error("Failed to get Whole Society Posts", e);
            throw e;
        }
    }

    @Override
    public Page<SocietyPost> getCreatedAtDescPostsByPage(Pageable pageable) {
        try {
            Page<SocietyPost> posts = societyPostRepository.findAllByOrderByCreatedAtDesc(pageable);
            log.info("Successfully got pageable Society Posts order by createdAt Desc");
            return posts;
        } catch (Exception e) {
            log.error("Failed to get pageable Society Posts order by createdAt Desc", e);
            throw e;
        }
    }

    @Override
    public Page<SocietyPost> getCreatedAtDescPostsByPageNSubCategory(SubCategory subCategory, Pageable pageable) {
        try {
            Page<SocietyPost> posts = societyPostRepository.findAllBySubCategoryOrderByCreatedAtDesc(subCategory, pageable);
            log.info("Successfully got pageable Society Posts order by createdAt Desc and subcategory: {}", subCategory);
            return posts;
        } catch (Exception e) {
            log.error("Failed to get pageable Society Posts order by createdAt Desc and subcategory: {}", subCategory, e);
            throw e;
        }
    }

    @Override
    public List<SocietyPost> getRecent5Posts() {
        try {
            List<SocietyPost> posts = societyPostRepository.findTop5ByOrderByCreatedAtDesc();
            log.info("Successfully got Recent 5 Society Posts");
            return posts;
        } catch (Exception e) {
            log.error("Failed to get Recent 5 Society Posts", e);
            throw e;
        }
    }

    @Override
    public SocietyPost getPostById(Long id) {
        try {
            Optional<SocietyPost> s = societyPostRepository.findById(id);
            if (s.isPresent()) {
                log.info("Successfully found society post with id: {}", id);
                return s.get();
            }
            throw new SocietyPostNotFoundException("Society Post not found with id: " + id);
        } catch (Exception e) {
            log.error("Failed to find society post with id: {}", id, e);
            throw e;
        }
    }

    @Override
    public Post createPost(SocietyPostDto societyPostDto, MultipartFile[] images) {
        User user = userService.getUserById(societyPostDto.getUserId());
        try {
            SocietyPost societyPost = SocietyPost.builder()
                    .title(societyPostDto.getTitle())
                    .description(societyPostDto.getDescription())
                    .category(Category.생활)
                    .subCategory(societyPostDto.getSubCategory())
                    .postType(PostType.NORMAL)
                    .contact(societyPostDto.getContact())
                    .email(societyPostDto.getEmail())
                    .suburb(societyPostDto.getSuburb())
                    .build();

            societyPost.setUser(user);
            SocietyPost createdPost = societyPostRepository.save(societyPost);

            // save post images data
            if (images != null) {
                Arrays.stream(images)
                        .map(image -> awsFileService.uploadPostFile(image, user.getEmail()))
                        .forEach(imageUrl -> imageService.createImage(imageUrl, createdPost));
            }

            log.info("Successfully created society post");

            return createdPost;
        } catch (Exception e) {
            log.error("Failed to create society post", e);
            return null;
        }
    }
}
