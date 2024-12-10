package com.promo.web.service;

import com.promo.web.aws.s3.AwsFileService;
import com.promo.web.dto.request.EducationPostDto;
import com.promo.web.entity.*;
import com.promo.web.exception.EducationPostNotFoundException;
import com.promo.web.repository.EducationPostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class EducationPostServiceImpl implements EducationPostService {

    private final EducationPostRepository educationPostRepository;
    private final UserService userService;
    private final TagService tagService;
    private final ImageService imageService;
    private final VideoService videoService;
    private final AwsFileService awsFileService;

    @Autowired
    public EducationPostServiceImpl(EducationPostRepository educationPostRepository, UserService userService, TagService tagService, ImageService imageService, VideoService videoService, AwsFileService awsFileService) {
        this.educationPostRepository = educationPostRepository;
        this.userService = userService;
        this.tagService = tagService;
        this.imageService = imageService;
        this.videoService = videoService;
        this.awsFileService = awsFileService;
    }

    @Override
    public List<EducationPost> getWholePosts() {
        try {
            List<EducationPost> posts = educationPostRepository.findAll();
            log.info("Successfully got Whole Education Posts");
            return posts;
        } catch (Exception e) {
            log.error("Failed to get Whole Education Posts", e);
            throw e;
        }
    }

    @Override
    public EducationPost getPostById(Long id) {
        try {
            Optional<EducationPost> p = educationPostRepository.findById(id);
            if (p.isPresent()) {
                log.info("Successfully found education post with id: {}", id);
                return p.get();
            }
            throw new EducationPostNotFoundException("Education Post not found with id: " + id);
        } catch (Exception e) {
            log.error("Failed to find education post with id: {}", id, e);
            throw e;
        }
    }

    @Override
    public Post createPost(String email, EducationPostDto educationPostDto, MultipartFile logoImage, MultipartFile[] images) {
        User user = userService.getUserByEmail(email);
        try {
            EducationPost educationPost = EducationPost.builder()
                    .title(educationPostDto.getTitle())
                    .subtitle(educationPostDto.getSubTitle())
                    .description(educationPostDto.getDescription())
                    .category(Category.Technology)
                    .subCategory(educationPostDto.getSubCategory())
                    .visibility(Visibility.valueOf(educationPostDto.getVisibility()))
                    .isOwnWork(educationPostDto.getIsOwnWork())
                    .ownerEmail(educationPostDto.getOwnerEmail())
                    .isPortrait(educationPostDto.getIsPortrait())
                    .webUrl(educationPostDto.getWebUrl())
                    .build();

            // save logo image data
            if (logoImage != null) {
                String logoImageUrl = awsFileService.uploadPostFile(logoImage, email);
                educationPost.setLogoUrl(logoImageUrl);
            }

            if (educationPostDto.getTags().size() > 0) {
                Set<Tag> tags = educationPostDto.getTags().stream()
                        .map(tagName -> {
                            Tag tag = tagService.getTagByName(tagName)
                                    .orElseGet(() -> tagService.createTag(new Tag(null, tagName, new HashSet<>())));

                            tag.getPosts().add(educationPost);
                            return tag;
                        })
                        .collect(Collectors.toSet());
                educationPost.setTags(tags);
            }

            educationPost.setUser(user);
            EducationPost createdPost = educationPostRepository.save(educationPost);

            // save post images data
            if (images != null) {
                Arrays.stream(images)
                        .map(image -> awsFileService.uploadPostFile(image, email))
                        .forEach(imageUrl -> imageService.createImage(imageUrl, educationPost));
            }

            // save post videos data
            if (educationPostDto.getYoutubeUrl() != null && !educationPostDto.getYoutubeUrl().equals("")) {
                videoService.createVideo(educationPostDto.getYoutubeUrl(), educationPost);
            }

            log.info("Successfully created education post");

            return createdPost;
        } catch (Exception e) {
            log.error("Failed to create education post", e);
            return null;
        }
    }
}