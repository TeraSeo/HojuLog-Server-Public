package com.promo.web.service;

import com.promo.web.aws.s3.AwsFileService;
import com.promo.web.dto.request.RestaurantPostDto;
import com.promo.web.entity.*;
import com.promo.web.exception.RestaurantPostNotFoundException;
import com.promo.web.repository.RestaurantPostRepository;
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
public class RestaurantPostServiceImpl implements RestaurantPostService {

    private final RestaurantPostRepository restaurantPostRepository;
    private final UserService userService;
    private final TagService tagService;
    private final ImageService imageService;
    private final VideoService videoService;
    private final AwsFileService awsFileService;

    @Autowired
    public RestaurantPostServiceImpl(RestaurantPostRepository restaurantPostRepository, UserService userService, TagService tagService, ImageService imageService, VideoService videoService, AwsFileService awsFileService) {
        this.restaurantPostRepository = restaurantPostRepository;
        this.userService = userService;
        this.tagService = tagService;
        this.imageService = imageService;
        this.videoService = videoService;
        this.awsFileService = awsFileService;
    }

    @Override
    public List<RestaurantPost> getWholePosts() {
        try {
            List<RestaurantPost> posts = restaurantPostRepository.findAll();
            log.info("Successfully got Whole Restaurant Posts");
            return posts;
        } catch (Exception e) {
            log.error("Failed to get Whole Restaurant Posts", e);
            throw e;
        }
    }

    @Override
    public RestaurantPost getPostById(Long id) {
        try {
            Optional<RestaurantPost> p = restaurantPostRepository.findById(id);
            if (p.isPresent()) {
                log.info("Successfully found restaurant post with id: {}", id);
                return p.get();
            }
            throw new RestaurantPostNotFoundException("Restaurant Post not found with id: " + id);
        } catch (Exception e) {
            log.error("Failed to find restaurant post with id: {}", id, e);
            throw e;
        }
    }

    @Override
    public Post createPost(String email, RestaurantPostDto restaurantPostDto, MultipartFile logoImage, MultipartFile[] images) {
        User user = userService.getUserByEmail(email);
        try {
            RestaurantPost restaurantPost = RestaurantPost.builder()
                    .title(restaurantPostDto.getTitle())
                    .subtitle(restaurantPostDto.getSubTitle())
                    .description(restaurantPostDto.getDescription())
                    .category(Category.Technology)
                    .subCategory(restaurantPostDto.getSubCategory())
                    .visibility(Visibility.valueOf(restaurantPostDto.getVisibility()))
                    .isOwnWork(restaurantPostDto.getIsOwnWork())
                    .ownerEmail(restaurantPostDto.getOwnerEmail())
                    .isPortrait(restaurantPostDto.getIsPortrait())
                    .webUrl(restaurantPostDto.getWebUrl())
                    .locationUrl(restaurantPostDto.getLocation())
                    .build();

            // save logo image data
            if (logoImage != null) {
                String logoImageUrl = awsFileService.uploadPostFile(logoImage, email);
                restaurantPost.setLogoUrl(logoImageUrl);
            }

            if (restaurantPostDto.getTags().size() > 0) {
                Set<Tag> tags = restaurantPostDto.getTags().stream()
                        .map(tagName -> {
                            Tag tag = tagService.getTagByName(tagName)
                                    .orElseGet(() -> tagService.createTag(new Tag(null, tagName, new HashSet<>())));

                            tag.getPosts().add(restaurantPost);
                            return tag;
                        })
                        .collect(Collectors.toSet());
                restaurantPost.setTags(tags);
            }

            restaurantPost.setUser(user);
            RestaurantPost createdPost = restaurantPostRepository.save(restaurantPost);

            // save post images data
            if (images != null) {
                Arrays.stream(images)
                        .map(image -> awsFileService.uploadPostFile(image, email))
                        .forEach(imageUrl -> imageService.createImage(imageUrl, restaurantPost));
            }

            // save post videos data
            if (restaurantPostDto.getYoutubeUrl() != null && !restaurantPostDto.getYoutubeUrl().equals("")) {
                videoService.createVideo(restaurantPostDto.getYoutubeUrl(), restaurantPost);
            }

            log.info("Successfully created restaurant post");
            return createdPost;
        } catch (Exception e) {
            log.error("Failed to create restaurant post", e);
            return null;
        }
    }
}
