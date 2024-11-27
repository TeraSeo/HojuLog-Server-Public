package com.promo.web.service;

import com.promo.web.aws.s3.AwsFileService;
import com.promo.web.dto.TechnologyPostDto;
import com.promo.web.entity.*;
import com.promo.web.exception.TechnologyPostNotFoundException;
import com.promo.web.repository.TechnologyPostRepository;
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
public class TechnologyPostServiceImpl implements TechnologyPostService {

    private final TechnologyPostRepository technologyPostRepository;
    private final UserService userService;
    private final TagService tagService;
    private final ImageService imageService;
    private final VideoService videoService;
    private final AwsFileService awsFileService;

    @Autowired
    public TechnologyPostServiceImpl(TechnologyPostRepository technologyPostRepository, UserService userService, TagService tagService, ImageService imageService, VideoService videoService, AwsFileService awsFileService) {
        this.technologyPostRepository = technologyPostRepository;
        this.userService = userService;
        this.tagService = tagService;
        this.imageService = imageService;
        this.videoService = videoService;
        this.awsFileService = awsFileService;
    }

    @Override
    public List<TechnologyPost> getWholePosts() {
        try {
            List<TechnologyPost> posts = technologyPostRepository.findAll();
            log.info("Successfully got Whole Technology Posts");
            return posts;
        } catch (Exception e) {
            log.error("Failed to get Whole Technology Posts", e);
            throw e;
        }
    }

    @Override
    public TechnologyPost getPostById(Long id) {
        try {
            Optional<TechnologyPost> p = technologyPostRepository.findById(id);
            if (p.isPresent()) {
                log.info("Successfully found technology post with id: {}", id);
                return p.get();
            }
            throw new TechnologyPostNotFoundException("Technology Post not found with id: " + id);
        } catch (Exception e) {
            log.error("Failed to find technology post with id: {}", id, e);
            throw e;
        }
    }

    @Override
    public Boolean createPost(String email, TechnologyPostDto technologyPostDto, MultipartFile logoImage, MultipartFile[] images, MultipartFile[] videos) {
        User user = userService.getUserByEmail(email);
        try {
            TechnologyPost technologyPost = TechnologyPost.builder()
                    .title(technologyPostDto.getTitle())
                    .subtitle(technologyPostDto.getSubTitle())
                    .description(technologyPostDto.getDescription())
                    .category(Category.Technology)
                    .subCategory(technologyPostDto.getSubCategory())
                    .visibility(Visibility.valueOf(technologyPostDto.getVisibility()))
                    .isOwnWork(technologyPostDto.getIsOwnWork())
                    .ownerEmail(technologyPostDto.getOwnerEmail())
                    .isPortrait(technologyPostDto.getIsPortrait())
                    .playStoreUrl(technologyPostDto.getPlayStoreUrl())
                    .appStoreUrl(technologyPostDto.getAppStoreUrl())
                    .webUrl(technologyPostDto.getWebUrl())
                    .build();

            // save logo image data
            if (logoImage != null) {
                String logoImageUrl = awsFileService.uploadPostFile(logoImage, email);
                technologyPost.setLogoUrl(logoImageUrl);
            }

            // save tags data
            if (technologyPostDto.getTags().size() > 0) {
                Set<Tag> tags = technologyPostDto.getTags().stream()
                        .map(tagName -> {
                            Tag tag = tagService.getTagByName(tagName)
                                    .orElseGet(() -> tagService.createTag(new Tag(null, tagName, new HashSet<>())));

                            tag.getPosts().add(technologyPost);
                            return tag;
                        })
                        .collect(Collectors.toSet());
                technologyPost.setTags(tags);
            }

            technologyPost.setUser(user);
            technologyPostRepository.save(technologyPost);

            // save post images data
            if (images != null) {
                Arrays.stream(images)
                        .map(image -> awsFileService.uploadPostFile(image, email))
                        .forEach(imageUrl -> imageService.createImage(imageUrl, technologyPost));
            }

            // save post videos data
            if (videos != null) {
                Arrays.stream(videos)
                        .map(video -> awsFileService.uploadPostFile(video, email))
                        .forEach(videoUrl -> videoService.createVideo(videoUrl, technologyPost));
            }

            log.info("Successfully created technology post");
            return true;
        } catch (Exception e) {
            log.error("Failed to create technology post", e);
            return false;
        }
    }
}
