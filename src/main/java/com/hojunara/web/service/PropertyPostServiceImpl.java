package com.hojunara.web.service;

import com.hojunara.web.aws.s3.AwsFileService;
import com.hojunara.web.dto.request.PropertyPostDto;
import com.hojunara.web.dto.request.UpdatePropertyMainInfoPostDto;
import com.hojunara.web.dto.request.UpdatePropertyPostDto;
import com.hojunara.web.entity.*;
import com.hojunara.web.exception.PropertyPostNotFoundException;
import com.hojunara.web.repository.PropertyPostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class PropertyPostServiceImpl implements PropertyPostService {

    private final PropertyPostRepository propertyPostRepository;
    private final UserService userService;
    private final AwsFileService awsFileService;
    private final ImageService imageService;
    private final KeywordService keywordService;

    @Autowired
    public PropertyPostServiceImpl(PropertyPostRepository propertyPostRepository, UserService userService, AwsFileService awsFileService, ImageService imageService, KeywordService keywordService) {
        this.propertyPostRepository = propertyPostRepository;
        this.userService = userService;
        this.awsFileService = awsFileService;
        this.imageService = imageService;
        this.keywordService = keywordService;
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
    public Page<PropertyPost> getCreatedAtDescPostsByPage(Pageable pageable) {
        try {
            Page<PropertyPost> posts = propertyPostRepository.findAllByOrderByCreatedAtDesc(pageable);
            log.info("Successfully got pageable Property Posts order by createdAt Desc");
            return posts;
        } catch (Exception e) {
            log.error("Failed to get pageable Property Posts order by createdAt Desc", e);
            throw e;
        }
    }

    @Override
    public Page<PropertyPost> getCreatedAtDescPostsByPageNSubCategory(SubCategory subCategory, Pageable pageable) {
        try {
            Page<PropertyPost> posts = propertyPostRepository.findAllBySubCategoryOrderByCreatedAtDesc(subCategory, pageable);
            log.info("Successfully got pageable Property Posts order by createdAt Desc and subCategory: {}", subCategory);
            return posts;
        } catch (Exception e) {
            log.error("Failed to get pageable Property Posts order by createdAt Desc and subCategory: {}", subCategory, e);
            throw e;
        }
    }

    @Override
    public List<PropertyPost> getRecent5Posts() {
        try {
            List<PropertyPost> posts = propertyPostRepository.findTop5ByOrderByCreatedAtDesc();
            log.info("Successfully got Recent 5 Property Posts");
            return posts;
        } catch (Exception e) {
            log.error("Failed to get Recent 5 Property Posts", e);
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
                    .postType(PostType.NORMAL)
                    .contact(propertyPostDto.getContact())
                    .email(propertyPostDto.getEmail())
                    .period(propertyPostDto.getPeriod())
                    .price(propertyPostDto.getPrice())
                    .location(propertyPostDto.getLocation())
                    .availableTime(propertyPostDto.getAvailableTime())
                    .suburb(propertyPostDto.getSuburb())
                    .roomCount(propertyPostDto.getRoomCount())
                    .bathroomType(propertyPostDto.getBathroomType())
                    .isParkable(propertyPostDto.getIsParkable())
                    .isBillIncluded(propertyPostDto.getIsBillIncluded())
                    .isCommentAllowed(propertyPostDto.getIsCommentAllowed())
                    .build();

            propertyPost.setUser(user);
            PropertyPost createdPost = propertyPostRepository.save(propertyPost);

            // save post images data
            if (images != null) {
                Arrays.stream(images)
                        .map(image -> awsFileService.uploadPostFile(image, user.getEmail()))
                        .forEach(imageUrl -> imageService.createImage(imageUrl, createdPost));
            }

            // save keywords
            propertyPostDto.getSelectedKeywords().stream().forEach(
                    keyword -> keywordService.createKeyword(keyword, createdPost)
            );

            log.info("Successfully created property post");

            return createdPost;
        } catch (Exception e) {
            log.error("Failed to create property post", e);
            return null;
        }
    }

    @Override
    public Post updatePost(UpdatePropertyPostDto updatePropertyPostDto, MultipartFile[] images) {
        UpdatePropertyMainInfoPostDto updatePropertyMainInfoPostDto = updatePropertyPostDto.getUpdatePropertyMainInfoPostDto();
        User user = userService.getUserById(updatePropertyMainInfoPostDto.getUserId());
        try {
            PropertyPost propertyPost = getPostById(updatePropertyMainInfoPostDto.getPostId());

            boolean isUpdated = false;

            if (!propertyPost.getTitle().equals(updatePropertyMainInfoPostDto.getTitle())) {
                propertyPost.setTitle(updatePropertyMainInfoPostDto.getTitle());
                isUpdated = true;
            }
            if (!Objects.equals(propertyPost.getDescription(), updatePropertyMainInfoPostDto.getDescription())) {
                propertyPost.setDescription(updatePropertyMainInfoPostDto.getDescription());
                isUpdated = true;
            }
            if (!Objects.equals(propertyPost.getContact(), updatePropertyMainInfoPostDto.getContact())) {
                propertyPost.setContact(updatePropertyMainInfoPostDto.getContact());
                isUpdated = true;
            }
            if (!Objects.equals(propertyPost.getEmail(), updatePropertyMainInfoPostDto.getEmail())) {
                propertyPost.setEmail(updatePropertyMainInfoPostDto.getEmail());
                isUpdated = true;
            }
            if (!Objects.equals(propertyPost.getSuburb(), updatePropertyMainInfoPostDto.getSuburb())) {
                propertyPost.setSuburb(updatePropertyMainInfoPostDto.getSuburb());
                isUpdated = true;
            }
            if (!Objects.equals(propertyPost.getPeriod(), updatePropertyMainInfoPostDto.getPeriod())) {
                propertyPost.setPeriod(updatePropertyMainInfoPostDto.getPeriod());
                isUpdated = true;
            }
            if (propertyPost.getPrice() != updatePropertyMainInfoPostDto.getPrice()) {
                propertyPost.setPrice(updatePropertyMainInfoPostDto.getPrice());
                isUpdated = true;
            }
            if (!propertyPost.getLocation().equals(updatePropertyMainInfoPostDto.getLocation())) {
                propertyPost.setLocation(updatePropertyMainInfoPostDto.getLocation());
                isUpdated = true;
            }
            if (!propertyPost.getAvailableTime().equals(updatePropertyMainInfoPostDto.getAvailableTime())) {
                propertyPost.setAvailableTime(updatePropertyMainInfoPostDto.getAvailableTime());
                isUpdated = true;
            }
            if (!propertyPost.getRoomCount().equals(updatePropertyMainInfoPostDto.getRoomCount())) {
                propertyPost.setRoomCount(updatePropertyMainInfoPostDto.getRoomCount());
                isUpdated = true;
            }
            if (!Objects.equals(propertyPost.getBathroomType(), updatePropertyMainInfoPostDto.getBathroomType())) {
                propertyPost.setBathroomType(updatePropertyMainInfoPostDto.getBathroomType());
                isUpdated = true;
            }
            if (!propertyPost.getIsParkable().equals(updatePropertyMainInfoPostDto.getIsParkable())) {
                propertyPost.setIsParkable(updatePropertyMainInfoPostDto.getIsParkable());
                isUpdated = true;
            }
            if (!Objects.equals(propertyPost.getIsBillIncluded(), updatePropertyMainInfoPostDto.getIsBillIncluded())) {
                propertyPost.setIsBillIncluded(updatePropertyMainInfoPostDto.getIsBillIncluded());
                isUpdated = true;
            }
            if (!propertyPost.getIsCommentAllowed().equals(updatePropertyMainInfoPostDto.getIsCommentAllowed())) {
                propertyPost.setIsCommentAllowed(updatePropertyMainInfoPostDto.getIsCommentAllowed());
                isUpdated = true;
            }

            List<String> imageUrls = propertyPost.getImages().stream().map(Image::getUrl).collect(Collectors.toList());
            List<String> updatedExistingImageUrls = updatePropertyPostDto.getUpdatePropertyMediaInfoPostDto().getExistingImages();
            if (!imageUrls.equals(updatedExistingImageUrls)) {
                List<String> removedImageUrls = imageUrls.stream()
                        .filter(imageUrl -> !updatedExistingImageUrls.contains(imageUrl))
                        .collect(Collectors.toList());

                if (!removedImageUrls.isEmpty()) {
                    propertyPost.getImages().removeIf(image -> removedImageUrls.contains(image.getUrl()));
                }
            }

            // 키워드 업데이트
            List<String> originalKeywords = propertyPost.getKeywords().stream().map(Keyword::getKeyWord).collect(Collectors.toList());
            List<String> updatedKeywords = updatePropertyMainInfoPostDto.getSelectedKeywords();
            if (!originalKeywords.equals(updatedKeywords)) {
                List<String> addedKeywords = updatedKeywords.stream()
                        .filter(keyword -> !originalKeywords.contains(keyword))
                        .collect(Collectors.toList());

                List<String> removedKeywords = originalKeywords.stream()
                        .filter(keyword -> !updatedKeywords.contains(keyword))
                        .collect(Collectors.toList());

                if (!addedKeywords.isEmpty()) {
                    addedKeywords.forEach(keyword -> {
                        keywordService.createKeyword(keyword, propertyPost);
                    });
                }

                if (!removedKeywords.isEmpty()) {
                    propertyPost.getKeywords().removeIf(keyword -> removedKeywords.contains(keyword.getKeyWord()));
                }

                isUpdated = true;
            }

            if (isUpdated) {
                propertyPostRepository.save(propertyPost);
                propertyPostRepository.flush(); // 업데이트 내용 반영
            }

            // save post images data
            if (images != null) {
                PropertyPost createdPost = getPostById(updatePropertyMainInfoPostDto.getPostId());
                Arrays.stream(images)
                        .map(image -> awsFileService.uploadPostFile(image, user.getEmail()))
                        .forEach(imageUrl -> imageService.createImage(imageUrl, createdPost));
            }

            log.info("Successfully updated property post");

            return propertyPost;
        } catch (Exception e) {
            log.error("Failed to update property post", e);
            return null;
        }
    }
}
