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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.ZoneId;
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
    public Page<PropertyPost> getCreatedAtDescPostsByPage(Pageable pageable, String option) {
        try {
            Page<PropertyPost> posts;
            if (option.equals("최신순")) {
                posts = propertyPostRepository.findAllWithPinnedFirst(pageable);
            }
            else if (option.equals("좋아요순")) {
                posts = propertyPostRepository.findAllWithPinnedFirstOrderByLikeCountDesc(pageable);
            }
            else {
                posts = propertyPostRepository.findAllWithPinnedFirstOrderByViewCountsDesc(pageable);
            }
            log.info("Successfully got pageable Property Posts order by option: {}", option);
            return posts;
        } catch (Exception e) {
            log.error("Failed to get pageable Property Posts order by option: {}", option, e);
            throw e;
        }
    }

    @Override
    public Page<PropertyPost> getCreatedAtDescPostsByPageNMinMaxPrice(Long minPrice, Long maxPrice, Pageable pageable, String option) {
        try {
            Page<PropertyPost> posts;
            if (option.equals("최신순")) {
                posts = propertyPostRepository.findAllWithPinnedFirstByPriceBetween(minPrice, maxPrice, pageable);
            }
            else if (option.equals("좋아요순")) {
                posts = propertyPostRepository.findAllWithPinnedFirstByPriceBetweenOrderByLikeCountDesc(minPrice, maxPrice, pageable);
            }
            else {
                posts = propertyPostRepository.findAllWithPinnedFirstByPriceBetweenOrderByViewCountsDesc(minPrice, maxPrice, pageable);
            }
            log.info("Successfully got pageable Property Posts order by option: {}, min price: {} and max price: {}", option, minPrice, maxPrice);
            return posts;
        } catch (Exception e) {
            log.error("Failed to get pageable Property Posts order by option: {}, min price: {} and max price: {}", option, minPrice, maxPrice, e);
            throw e;
        }
    }

    @Override
    public Page<PropertyPost> getCreatedAtDescPostsByPageNPeriod(Period period, Pageable pageable, String option) {
        try {
            Page<PropertyPost> posts;
            if (option.equals("최신순")) {
                posts = propertyPostRepository.findAllWithPinnedFirstByPeriod(period, pageable);
            }
            else if (option.equals("좋아요순")) {
                posts = propertyPostRepository.findAllWithPinnedFirstByPeriodOrderByLikeCountDesc(period, pageable);
            }
            else {
                posts = propertyPostRepository.findAllWithPinnedFirstByPeriodOrderByViewCountsDesc(period, pageable);
            }
            log.info("Successfully got pageable Property Posts order by option: {} and period: {}", option, period);
            return posts;
        } catch (Exception e) {
            log.error("Failed to get pageable Property Posts order by option: {} and period: {}", option, period, e);
            throw e;
        }
    }

    @Override
    public Page<PropertyPost> getCreatedAtDescPostsByPageNMinMaxPriceNPeriod(Long minPrice, Long maxPrice, Period period, Pageable pageable, String option) {
        try {
            Page<PropertyPost> posts;
            if (option.equals("최신순")) {
                posts = propertyPostRepository.findAllWithPinnedFirstByPriceBetweenAndPeriod(minPrice, maxPrice, period, pageable);
            }
            else if (option.equals("좋아요순")) {
                posts = propertyPostRepository.findAllWithPinnedFirstByPriceBetweenAndPeriodOrderByLikesDesc(minPrice, maxPrice, period, pageable);
            }
            else {
                posts = propertyPostRepository.findAllWithPinnedFirstByPriceBetweenAndPeriodOrderByViewCountsDesc(minPrice, maxPrice, period, pageable);
            }
            log.info("Successfully got pageable Property Posts order by option: {}, min price: {}, price: {} and period: {}", option, minPrice, maxPrice, period);
            return posts;
        } catch (Exception e) {
            log.error("Failed to get pageable Property Posts order by option: {}, min price: {}, max price: {} and period: {}", option, minPrice, maxPrice, period, e);
            throw e;
        }
    }

    @Override
    public Page<PropertyPost> getCreatedAtDescPostsByPageNSubCategory(SubCategory subCategory, Pageable pageable, String option) {
        try {
            Page<PropertyPost> posts;
            if (option.equals("최신순")) {
                posts = propertyPostRepository.findAllBySubCategoryOrderByUpdatedAtDesc(subCategory, pageable);
            }
            else if (option.equals("좋아요순")) {
                posts = propertyPostRepository.findAllBySubCategoryOrderByLikesDesc(subCategory, pageable);
            }
            else {
                posts = propertyPostRepository.findAllBySubCategoryOrderByViewCountsDesc(subCategory, pageable);
            }
            log.info("Successfully got pageable Property Posts order by option: {} and subCategory: {}", option, subCategory);
            return posts;
        } catch (Exception e) {
            log.error("Failed to get pageable Property Posts order by option: {} and subCategory: {}", option, subCategory, e);
            throw e;
        }
    }

    @Override
    public Page<PropertyPost> getCreatedAtDescPostsByPageNSubCategoryNMinMaxPrice(Long minPrice, Long maxPrice, SubCategory subCategory, Pageable pageable, String option) {
        try {
            Page<PropertyPost> posts;
            if (option.equals("최신순")) {
                posts = propertyPostRepository.findAllBySubCategoryAndPriceBetweenOrderByUpdatedAtDesc(subCategory, minPrice, maxPrice, pageable);
            }
            else if (option.equals("좋아요순")) {
                posts = propertyPostRepository.findAllBySubCategoryAndPriceBetweenOrderByLikesDesc(subCategory, minPrice, maxPrice, pageable);
            }
            else {
                posts = propertyPostRepository.findAllBySubCategoryAndPriceBetweenOrderByViewCountsDesc(subCategory, minPrice, maxPrice, pageable);
            }
            log.info("Successfully got pageable Property Posts order by option: {}, subCategory: {}, min price: {} and max price: {}", option, subCategory, minPrice, maxPrice);
            return posts;
        } catch (Exception e) {
            log.error("Failed to get pageable Property Posts order by option: {}, subCategory: {}, min price: {} and max price: {}", option, subCategory, minPrice, maxPrice, e);
            throw e;
        }
    }

    @Override
    public Page<PropertyPost> getCreatedAtDescPostsByPageNSubCategoryNPeriod(Period period, SubCategory subCategory, Pageable pageable, String option) {
        try {
            Page<PropertyPost> posts;
            if (option.equals("최신순")) {
                posts = propertyPostRepository.findAllBySubCategoryAndPeriodOrderByUpdatedAtDesc(subCategory, period, pageable);
            }
            else if (option.equals("좋아요순")) {
                posts = propertyPostRepository.findAllBySubCategoryAndPeriodOrderByLikesDesc(subCategory, period, pageable);
            }
            else {
                posts = propertyPostRepository.findAllBySubCategoryAndPeriodOrderByViewCountsDesc(subCategory, period, pageable);
            }
            log.info("Successfully got pageable Property Posts order by option: {}, subCategory: {} and period: {}", option, subCategory, period);
            return posts;
        } catch (Exception e) {
            log.error("Failed to get pageable Property Posts order by option: {}, subCategory: {} and period: {}", option, subCategory, period, e);
            throw e;
        }
    }

    @Override
    public Page<PropertyPost> getCreatedAtDescPostsByPageNSubCategoryNMinMaxPriceNPeriod(Long minPrice, Long maxPrice, Period period, SubCategory subCategory, Pageable pageable, String option) {
        try {
            Page<PropertyPost> posts;
            if (option.equals("최신순")) {
                posts = propertyPostRepository.findAllBySubCategoryAndPriceBetweenAndPeriodOrderByUpdatedAtDesc(subCategory, minPrice, maxPrice, period, pageable);
            }
            else if (option.equals("좋아요순")) {
                posts = propertyPostRepository.findAllBySubCategoryAndPriceBetweenAndPeriodOrderByLikesDesc(subCategory, minPrice, maxPrice, period, pageable);
            }
            else {
                posts = propertyPostRepository.findAllBySubCategoryAndPriceBetweenAndPeriodOrderByViewCountsDesc(subCategory, minPrice, maxPrice, period, pageable);
            }
            log.info("Successfully got pageable Property Posts order by option: {}, subCategory: {}, min price: {}, max price: {} and period: {}", option, subCategory, minPrice, maxPrice, period);
            return posts;
        } catch (Exception e) {
            log.error("Failed to get pageable Property Posts order by option: {}, subCategory: {}, min price: {}, max price: {} and period: {}", option, subCategory, minPrice, maxPrice, period, e);
            throw e;
        }
    }

    @Override
    public List<PropertyPost> getRecent5Posts() {
        try {
            List<PropertyPost> posts = propertyPostRepository.findTop5ByOrderByUpdatedAtDesc();
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
                    .viewCounts(0L)
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
        PropertyPost propertyPost = getPostById(updatePropertyMainInfoPostDto.getPostId());
        try {
            if (!propertyPost.getTitle().equals(updatePropertyMainInfoPostDto.getTitle())) {
                propertyPost.setTitle(updatePropertyMainInfoPostDto.getTitle());
            }
            if (!Objects.equals(propertyPost.getDescription(), updatePropertyMainInfoPostDto.getDescription())) {
                propertyPost.setDescription(updatePropertyMainInfoPostDto.getDescription());
            }
            if (!Objects.equals(propertyPost.getContact(), updatePropertyMainInfoPostDto.getContact())) {
                propertyPost.setContact(updatePropertyMainInfoPostDto.getContact());
            }
            if (!Objects.equals(propertyPost.getEmail(), updatePropertyMainInfoPostDto.getEmail())) {
                propertyPost.setEmail(updatePropertyMainInfoPostDto.getEmail());
            }
            if (!Objects.equals(propertyPost.getSuburb(), updatePropertyMainInfoPostDto.getSuburb())) {
                propertyPost.setSuburb(updatePropertyMainInfoPostDto.getSuburb());
            }
            if (!Objects.equals(propertyPost.getPeriod(), updatePropertyMainInfoPostDto.getPeriod())) {
                propertyPost.setPeriod(updatePropertyMainInfoPostDto.getPeriod());
            }
            if (propertyPost.getPrice() != updatePropertyMainInfoPostDto.getPrice()) {
                propertyPost.setPrice(updatePropertyMainInfoPostDto.getPrice());
            }
            if (!propertyPost.getLocation().equals(updatePropertyMainInfoPostDto.getLocation())) {
                propertyPost.setLocation(updatePropertyMainInfoPostDto.getLocation());
            }
            if (!propertyPost.getAvailableTime().equals(updatePropertyMainInfoPostDto.getAvailableTime())) {
                propertyPost.setAvailableTime(updatePropertyMainInfoPostDto.getAvailableTime());
            }
            if (!propertyPost.getRoomCount().equals(updatePropertyMainInfoPostDto.getRoomCount())) {
                propertyPost.setRoomCount(updatePropertyMainInfoPostDto.getRoomCount());
            }
            if (!Objects.equals(propertyPost.getBathroomType(), updatePropertyMainInfoPostDto.getBathroomType())) {
                propertyPost.setBathroomType(updatePropertyMainInfoPostDto.getBathroomType());
            }
            if (!propertyPost.getIsParkable().equals(updatePropertyMainInfoPostDto.getIsParkable())) {
                propertyPost.setIsParkable(updatePropertyMainInfoPostDto.getIsParkable());
            }
            if (!Objects.equals(propertyPost.getIsBillIncluded(), updatePropertyMainInfoPostDto.getIsBillIncluded())) {
                propertyPost.setIsBillIncluded(updatePropertyMainInfoPostDto.getIsBillIncluded());
            }
            if (!propertyPost.getIsCommentAllowed().equals(updatePropertyMainInfoPostDto.getIsCommentAllowed())) {
                propertyPost.setIsCommentAllowed(updatePropertyMainInfoPostDto.getIsCommentAllowed());
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
            List<String> updatedKeywords = updatePropertyMainInfoPostDto.getSelectedKeywords();
            keywordService.updateKeyword(propertyPost, updatedKeywords);

            final ZoneId SYDNEY_ZONE = ZoneId.of("Australia/Sydney");
            propertyPost.setUpdatedAt(Timestamp.from(java.time.ZonedDateTime.now(SYDNEY_ZONE).toInstant()));
            propertyPostRepository.save(propertyPost);
            propertyPostRepository.flush(); // 업데이트 내용 반영

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

    @Override
    public List<PropertyPost> searchPropertyPost(String title, String subCategory, String suburb, List<String> keywords) {
        boolean isTitleEmpty = title == null || title.equals("");
        boolean isSubCategoryEmpty = subCategory == null || subCategory.equals("");
        boolean isSuburbEmpty = suburb == null || suburb.equals("");

        List<PropertyPost> propertyPostList;
        if (isTitleEmpty && isSubCategoryEmpty && isSuburbEmpty) propertyPostList = searchByCategory();
        else if (isSubCategoryEmpty && isSuburbEmpty) propertyPostList = searchByTitle(title);
        else if (isTitleEmpty && isSuburbEmpty) propertyPostList = searchBySubCategory(SubCategory.valueOf(subCategory));
        else if (isTitleEmpty && isSubCategoryEmpty) propertyPostList = searchBySuburb(Suburb.valueOf(suburb));
        else if (isSuburbEmpty) propertyPostList = searchByTitleAndSubCategory(title, SubCategory.valueOf(subCategory));
        else if (isTitleEmpty) propertyPostList = searchBySubCategoryAndSuburb(SubCategory.valueOf(subCategory), Suburb.valueOf(suburb));
        else if (isSubCategoryEmpty) propertyPostList = searchByTitleAndSuburb(title, Suburb.valueOf(suburb));
        else propertyPostList = searchByTitleAndSubCategoryAndSuburb(title, SubCategory.valueOf(subCategory), Suburb.valueOf(suburb));

        if (keywords != null && !keywords.isEmpty()) {
            propertyPostList = propertyPostList.stream()
                    .map(post -> {
                        List<String> postKeywords = post.getKeywords().stream()
                                .map(Keyword::getKeyWord)
                                .toList();

                        long matchCount = keywords.stream()
                                .filter(postKeywords::contains)
                                .count();

                        return new AbstractMap.SimpleEntry<>(post, matchCount);
                    })
                    .filter(entry -> {
                        long matchCount = entry.getValue();
                        int totalSearchedKeywords = keywords.size();
                        long inCorrectKeywordsCount = totalSearchedKeywords - matchCount;

                        if (totalSearchedKeywords >= 7) {
                            return inCorrectKeywordsCount <= 3;
                        }
                        if (totalSearchedKeywords >= 5) {
                            return inCorrectKeywordsCount <= 2;
                        } else if (totalSearchedKeywords >= 2) {
                            return inCorrectKeywordsCount <= 1;
                        }
                        return inCorrectKeywordsCount <= 0;
                    })
                    .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue())) // Sort by matchCount descending
                    .map(Map.Entry::getKey) // Extract the original post
                    .toList();
        }

        return propertyPostList;
    }

    @Override
    public List<PropertyPost> searchByCategory() {
        return propertyPostRepository.findAllByOrderByUpdatedAtDesc();
    }

    @Override
    public List<PropertyPost> searchByTitle(String title) {
        return propertyPostRepository.findByTitleContainingOrderByUpdatedAtDesc(title);
    }

    @Override
    public List<PropertyPost> searchBySubCategory(SubCategory subCategory) {
        return propertyPostRepository.findBySubCategoryOrderByUpdatedAtDesc(subCategory);
    }

    @Override
    public List<PropertyPost> searchBySuburb(Suburb suburb) {
        return propertyPostRepository.findBySuburbOrderByUpdatedAtDesc(suburb);
    }

    @Override
    public List<PropertyPost> searchByTitleAndSubCategory(String title, SubCategory subCategory) {
        return propertyPostRepository.findByTitleContainingAndSubCategoryOrderByUpdatedAtDesc(title, subCategory);
    }

    @Override
    public List<PropertyPost> searchByTitleAndSuburb(String title, Suburb suburb) {
        return propertyPostRepository.findByTitleContainingAndSuburbOrderByUpdatedAtDesc(title, suburb);
    }

    @Override
    public List<PropertyPost> searchBySubCategoryAndSuburb(SubCategory subCategory, Suburb suburb) {
        return propertyPostRepository.findBySubCategoryAndSuburbOrderByUpdatedAtDesc(subCategory, suburb);
    }

    @Override
    public List<PropertyPost> searchByTitleAndSubCategoryAndSuburb(String title, SubCategory subCategory, Suburb suburb) {
        return propertyPostRepository.findByTitleContainingAndSubCategoryAndSuburbOrderByUpdatedAtDesc(title, subCategory, suburb);
    }

    @Override
    public Page<PropertyPost> convertPostsAsPage(List<PropertyPost> posts, Pageable pageable) {
        try {
            int start = (int) pageable.getOffset();
            int end = Math.min(start + pageable.getPageSize(), posts.size());

            List<PropertyPost> paginatedPosts = posts.subList(start, end);

            return new PageImpl<>(paginatedPosts, pageable, posts.size());
        } catch (Exception e) {
            log.error("Failed to convert Post as Page");
            throw e;
        }
    }
}
