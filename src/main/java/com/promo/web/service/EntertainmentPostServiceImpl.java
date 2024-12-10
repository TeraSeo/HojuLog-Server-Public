package com.promo.web.service;

import com.promo.web.aws.s3.AwsFileService;
import com.promo.web.dto.request.EntertainmentPostDto;
import com.promo.web.entity.*;
import com.promo.web.exception.EntertainmentPostNotFoundException;
import com.promo.web.repository.EntertainmentPostRepository;
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
public class EntertainmentPostServiceImpl implements EntertainmentPostService {

    private final EntertainmentPostRepository entertainmentPostRepository;
    private final UserService userService;
    private final TagService tagService;
    private final ImageService imageService;
    private final VideoService videoService;
    private final AwsFileService awsFileService;

    @Autowired
    public EntertainmentPostServiceImpl(EntertainmentPostRepository entertainmentPostRepository, UserService userService, TagService tagService, ImageService imageService, VideoService videoService, AwsFileService awsFileService) {
        this.entertainmentPostRepository = entertainmentPostRepository;
        this.userService = userService;
        this.tagService = tagService;
        this.imageService = imageService;
        this.videoService = videoService;
        this.awsFileService = awsFileService;
    }

    @Override
    public List<EntertainmentPost> getWholePosts() {
        try {
            List<EntertainmentPost> posts = entertainmentPostRepository.findAll();
            log.info("Successfully got Whole Entertainment Posts");
            return posts;
        } catch (Exception e) {
            log.error("Failed to get Whole Entertainment Posts", e);
            throw e;
        }
    }

    @Override
    public EntertainmentPost getPostById(Long id) {
        try {
            Optional<EntertainmentPost> p = entertainmentPostRepository.findById(id);
            if (p.isPresent()) {
                log.info("Successfully found entertainment post with id: {}", id);
                return p.get();
            }
            throw new EntertainmentPostNotFoundException("Entertainment Post not found with id: " + id);
        } catch (Exception e) {
            log.error("Failed to find entertainment post with id: {}", id, e);
            throw e;
        }
    }

    @Override
    public Post createPost(String email, EntertainmentPostDto entertainmentPostDto, MultipartFile logoImage, MultipartFile[] images) {
        User user = userService.getUserByEmail(email);
        try {
            EntertainmentPost entertainmentPost = EntertainmentPost.builder()
                    .title(entertainmentPostDto.getTitle())
                    .subtitle(entertainmentPostDto.getSubTitle())
                    .description(entertainmentPostDto.getDescription())
                    .category(Category.Technology)
                    .subCategory(entertainmentPostDto.getSubCategory())
                    .visibility(Visibility.valueOf(entertainmentPostDto.getVisibility()))
                    .isOwnWork(entertainmentPostDto.getIsOwnWork())
                    .ownerEmail(entertainmentPostDto.getOwnerEmail())
                    .isPortrait(entertainmentPostDto.getIsPortrait())
                    .webUrl(entertainmentPostDto.getWebUrl())
                    .startDateTime(entertainmentPostDto.getStartDateTime())
                    .endDateTime(entertainmentPostDto.getEndDateTime())
                    .location(entertainmentPostDto.getLocation())
                    .build();

            // save logo image data
            if (logoImage != null) {
                String logoImageUrl = awsFileService.uploadPostFile(logoImage, email);
                entertainmentPost.setLogoUrl(logoImageUrl);
            }

            if (entertainmentPostDto.getTags().size() > 0) {
                Set<Tag> tags = entertainmentPostDto.getTags().stream()
                        .map(tagName -> {
                            Tag tag = tagService.getTagByName(tagName)
                                    .orElseGet(() -> tagService.createTag(new Tag(null, tagName, new HashSet<>())));

                            tag.getPosts().add(entertainmentPost);
                            return tag;
                        })
                        .collect(Collectors.toSet());
                entertainmentPost.setTags(tags);
            }

            entertainmentPost.setUser(user);
            EntertainmentPost createdPost = entertainmentPostRepository.save(entertainmentPost);

            // save post images data
            if (images != null) {
                Arrays.stream(images)
                        .map(image -> awsFileService.uploadPostFile(image, email))
                        .forEach(imageUrl -> imageService.createImage(imageUrl, entertainmentPost));
            }

            // save post videos data
            if (entertainmentPostDto.getYoutubeUrl() != null && !entertainmentPostDto.getYoutubeUrl().equals("")) {
                videoService.createVideo(entertainmentPostDto.getYoutubeUrl(), entertainmentPost);
            }

            log.info("Successfully created entertainment post");
            return createdPost;
        } catch (Exception e) {
            log.error("Failed to create entertainment post", e);
            return null;
        }
    }
}