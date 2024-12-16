package com.hojunara.web.service;

import com.hojunara.web.aws.s3.AwsFileService;
import com.hojunara.web.dto.request.PropertyPostDto;
import com.hojunara.web.entity.Category;
import com.hojunara.web.entity.Post;
import com.hojunara.web.entity.PropertyPost;
import com.hojunara.web.entity.User;
import com.hojunara.web.exception.PropertyPostNotFoundException;
import com.hojunara.web.repository.PropertyPostRepository;
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
public class PropertyPostServiceImpl implements PropertyPostService {

    private final PropertyPostRepository propertyPostRepository;
    private final UserService userService;
    private final AwsFileService awsFileService;
    private final ImageService imageService;

    @Autowired
    public PropertyPostServiceImpl(PropertyPostRepository propertyPostRepository, UserService userService, AwsFileService awsFileService, ImageService imageService) {
        this.propertyPostRepository = propertyPostRepository;
        this.userService = userService;
        this.awsFileService = awsFileService;
        this.imageService = imageService;
    }

    @Override
    public List<PropertyPost> getWholePosts() {
        try {
            List<PropertyPost> posts = propertyPostRepository.findAll();
            log.info("Successfully got Whole Property Posts");
            return posts;
        } catch (Exception e) {
            log.error("Failed to get Whole Property Posts", e);
            throw e;
        }
    }

    @Override
    public PropertyPost getPostById(Long id) {
        try {
            Optional<PropertyPost> p = propertyPostRepository.findById(id);
            if (p.isPresent()) {
                log.info("Successfully found property post with id: {}", id);
                return p.get();
            }
            throw new PropertyPostNotFoundException("Property Post not found with id: " + id);
        } catch (Exception e) {
            log.error("Failed to find property post with id: {}", id, e);
            throw e;
        }
    }

    @Override
    public Post createPost(PropertyPostDto propertyPostDto, MultipartFile[] images) {
        User user = userService.getUserById(propertyPostDto.getUserId());
        try {
            PropertyPost propertyPost = PropertyPost.builder()
                    .title(propertyPostDto.getTitle())
                    .description(propertyPostDto.getDescription())
                    .category(Category.부동산)
                    .subCategory(propertyPostDto.getSubCategory())
                    .contact(propertyPostDto.getContact())
                    .email(propertyPostDto.getEmail())
                    .isPortrait(propertyPostDto.getIsPortrait())
                    .viewCounts(0L)
                    .period(propertyPostDto.getPeriod())
                    .price(propertyPostDto.getPrice())
                    .address(propertyPostDto.getAddress())
                    .availableTime(propertyPostDto.getAvailableTime())
                    .suburb(propertyPostDto.getSuburb())
                    .build();

            propertyPost.setUser(user);
            PropertyPost createdPost = propertyPostRepository.save(propertyPost);

            // save post images data
            if (images != null) {
                Arrays.stream(images)
                        .map(image -> awsFileService.uploadPostFile(image, user.getEmail()))
                        .forEach(imageUrl -> imageService.createImage(imageUrl, createdPost));
            }

            log.info("Successfully created property post");

            return createdPost;
        } catch (Exception e) {
            log.error("Failed to create property post", e);
            return null;
        }
    }
}
