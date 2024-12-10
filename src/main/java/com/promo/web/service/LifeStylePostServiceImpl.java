package com.promo.web.service;

import com.promo.web.aws.s3.AwsFileService;
import com.promo.web.dto.request.LifeStylePostDto;
import com.promo.web.entity.*;
import com.promo.web.exception.LifeStylePostNotFoundException;
import com.promo.web.repository.LifeStylePostRepository;
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
public class LifeStylePostServiceImpl implements LifeStylePostService{

    private final LifeStylePostRepository lifeStylePostRepository;
    private final UserService userService;
    private final TagService tagService;
    private final ImageService imageService;
    private final VideoService videoService;
    private final AwsFileService awsFileService;

    @Autowired
    public LifeStylePostServiceImpl(LifeStylePostRepository lifeStylePostRepository, UserService userService, TagService tagService, ImageService imageService, VideoService videoService, AwsFileService awsFileService) {
        this.lifeStylePostRepository = lifeStylePostRepository;
        this.userService = userService;
        this.tagService = tagService;
        this.imageService = imageService;
        this.videoService = videoService;
        this.awsFileService = awsFileService;
    }

    @Override
    public List<LifeStylePost> getWholePosts() {
        try {
            List<LifeStylePost> posts = lifeStylePostRepository.findAll();
            log.info("Successfully got Whole LifeStyle Posts");
            return posts;
        } catch (Exception e) {
            log.error("Failed to get Whole LifeStyle Posts", e);
            throw e;
        }
    }

    @Override
    public LifeStylePost getPostById(Long id) {
        try {
            Optional<LifeStylePost> p = lifeStylePostRepository.findById(id);
            if (p.isPresent()) {
                log.info("Successfully found lifestyle post with id: {}", id);
                return p.get();
            }
            throw new LifeStylePostNotFoundException("LifeStyle Post not found with id: " + id);
        } catch (Exception e) {
            log.error("Failed to find lifestyle post with id: {}", id, e);
            throw e;
        }
    }

    @Override
    public Post createPost(String email, LifeStylePostDto lifeStylePostDto, MultipartFile logoImage, MultipartFile[] images) {
        User user = userService.getUserByEmail(email);
        try {
            LifeStylePost lifeStylePost = LifeStylePost.builder()
                    .title(lifeStylePostDto.getTitle())
                    .subtitle(lifeStylePostDto.getSubTitle())
                    .description(lifeStylePostDto.getDescription())
                    .category(Category.Technology)
                    .subCategory(lifeStylePostDto.getSubCategory())
                    .visibility(Visibility.valueOf(lifeStylePostDto.getVisibility()))
                    .isOwnWork(lifeStylePostDto.getIsOwnWork())
                    .ownerEmail(lifeStylePostDto.getOwnerEmail())
                    .isPortrait(lifeStylePostDto.getIsPortrait())
                    .webUrl(lifeStylePostDto.getWebUrl())
                    .build();

            // save logo image data
            if (logoImage != null) {
                String logoImageUrl = awsFileService.uploadPostFile(logoImage, email);
                lifeStylePost.setLogoUrl(logoImageUrl);
            }

            if (lifeStylePostDto.getTags().size() > 0) {
                Set<Tag> tags = lifeStylePostDto.getTags().stream()
                        .map(tagName -> {
                            Tag tag = tagService.getTagByName(tagName)
                                    .orElseGet(() -> tagService.createTag(new Tag(null, tagName, new HashSet<>())));

                            tag.getPosts().add(lifeStylePost);
                            return tag;
                        })
                        .collect(Collectors.toSet());
                lifeStylePost.setTags(tags);
            }

            lifeStylePost.setUser(user);
            LifeStylePost createdPost = lifeStylePostRepository.save(lifeStylePost);

            // save post images data
            if (images != null) {
                Arrays.stream(images)
                        .map(image -> awsFileService.uploadPostFile(image, email))
                        .forEach(imageUrl -> imageService.createImage(imageUrl, lifeStylePost));
            }

            // save post videos data
            if (lifeStylePostDto.getYoutubeUrl() != null && !lifeStylePostDto.getYoutubeUrl().equals("")) {
                videoService.createVideo(lifeStylePostDto.getYoutubeUrl(), lifeStylePost);
            }

            log.info("Successfully created lifestyle post");

            return createdPost;
        } catch (Exception e) {
            log.error("Failed to create lifestyle post", e);
            return null;
        }
    }
}
