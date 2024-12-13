package com.hojunara.web.service;

import com.hojunara.web.aws.s3.AwsFileService;
import com.hojunara.web.dto.request.TravelPostDto;
import com.hojunara.web.entity.*;
import com.hojunara.web.exception.TravelPostNotFoundException;
import com.hojunara.web.repository.TravelPostRepository;
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
public class TravelPostServiceImpl implements TravelPostService {

    private final TravelPostRepository travelPostRepository;
    private final UserService userService;
    private final AwsFileService awsFileService;
    private final ImageService imageService;

    @Autowired
    public TravelPostServiceImpl(TravelPostRepository travelPostRepository, UserService userService, AwsFileService awsFileService, ImageService imageService) {
        this.travelPostRepository = travelPostRepository;
        this.userService = userService;
        this.awsFileService = awsFileService;
        this.imageService = imageService;
    }

    @Override
    public List<TravelPost> getWholePosts() {
        try {
            List<TravelPost> posts = travelPostRepository.findAll();
            log.info("Successfully got Whole Travel Posts");
            return posts;
        } catch (Exception e) {
            log.error("Failed to get Whole Travel Posts", e);
            throw e;
        }
    }

    @Override
    public TravelPost getPostById(Long id) {
        try {
            Optional<TravelPost> t = travelPostRepository.findById(id);
            if (t.isPresent()) {
                log.info("Successfully found travel post with id: {}", id);
                return t.get();
            }
            throw new TravelPostNotFoundException("Travel Post not found with id: " + id);
        } catch (Exception e) {
            log.error("Failed to find travel post with id: {}", id, e);
            throw e;
        }
    }

    @Override
    public Post createPost(TravelPostDto travelPostDto, MultipartFile[] images) {
        User user = userService.getUserById(travelPostDto.getUserId());
        try {
            TravelPost travelPost = TravelPost.builder()
                    .title(travelPostDto.getTitle())
                    .description(travelPostDto.getDescription())
                    .category(Category.여행)
                    .subCategory(travelPostDto.getSubCategory())
                    .contact(travelPostDto.getContact())
                    .email(travelPostDto.getEmail())
                    .isPortrait(travelPostDto.getIsPortrait())
                    .viewCounts(0L)
                    .address(travelPostDto.getAddress())
                    .country(travelPostDto.getCountry())
                    .build();

            travelPost.setUser(user);
            TravelPost createdPost = travelPostRepository.save(travelPost);

            // save post images data
            if (images != null) {
                Arrays.stream(images)
                        .map(image -> awsFileService.uploadPostFile(image, user.getEmail()))
                        .forEach(imageUrl -> imageService.createImage(imageUrl, createdPost));
            }

            log.info("Successfully created travel post");

            return createdPost;
        } catch (Exception e) {
            log.error("Failed to create travel post", e);
            return null;
        }
    }
}
