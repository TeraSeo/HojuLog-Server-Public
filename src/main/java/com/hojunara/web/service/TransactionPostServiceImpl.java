package com.hojunara.web.service;

import com.hojunara.web.aws.s3.AwsFileService;
import com.hojunara.web.dto.request.TransactionPostDto;
import com.hojunara.web.dto.request.UpdateTransactionMainInfoPostDto;
import com.hojunara.web.dto.request.UpdateTransactionPostDto;
import com.hojunara.web.entity.*;
import com.hojunara.web.exception.TransactionPostNotFoundException;
import com.hojunara.web.repository.TransactionPostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class TransactionPostServiceImpl implements TransactionPostService {

    private final TransactionPostRepository transactionPostRepository;
    private final UserService userService;
    private final AwsFileService awsFileService;
    private final ImageService imageService;
    private final KeywordService keywordService;

    @Autowired
    public TransactionPostServiceImpl(TransactionPostRepository transactionPostRepository, UserService userService, AwsFileService awsFileService, ImageService imageService, KeywordService keywordService) {
        this.transactionPostRepository = transactionPostRepository;
        this.userService = userService;
        this.awsFileService = awsFileService;
        this.imageService = imageService;
        this.keywordService = keywordService;
    }

    @Override
    public List<TransactionPost> getWholePosts() {
        try {
            List<TransactionPost> posts = transactionPostRepository.findAll();
            log.info("Successfully got Whole Transaction Posts");
            return posts;
        } catch (Exception e) {
            log.error("Failed to get Whole Transaction Posts", e);
            throw e;
        }
    }

    @Override
    public Page<TransactionPost> getCreatedAtDescPostsByPage(Pageable pageable) {
        try {
            Page<TransactionPost> posts = transactionPostRepository.findAllWithPinnedFirst(pageable);
            log.info("Successfully got pageable Transaction Posts order by createdAt Desc");
            return posts;
        } catch (Exception e) {
            log.error("Failed to get pageable Transaction Posts order by createdAt Desc", e);
            throw e;
        }
    }

    @Override
    public Page<TransactionPost> getCreatedAtDescPostsByPageNTransactionType(Pageable pageable, TransactionType transactionType) {
        try {
            Page<TransactionPost> posts = transactionPostRepository.findAllWithPinnedFirstByTransactionType(transactionType, pageable);
            log.info("Successfully got pageable Transaction Posts order by createdAt Desc and transactionType: {}", transactionType);
            return posts;
        } catch (Exception e) {
            log.error("Failed to get pageable Transaction Posts order by createdAt Desc and transactionType: {}", transactionType, e);
            throw e;
        }
    }

    @Override
    public Page<TransactionPost> getCreatedAtDescPostsByPageNPriceType(Pageable pageable, PriceType priceType) {
        try {
            Page<TransactionPost> posts = transactionPostRepository.findAllWithPinnedFirstByPriceType(priceType, pageable);
            log.info("Successfully got pageable Transaction Posts order by createdAt Desc and priceType: {}", priceType);
            return posts;
        } catch (Exception e) {
            log.error("Failed to get pageable Transaction Posts order by createdAt Desc and priceType: {}", priceType, e);
            throw e;
        }
    }

    @Override
    public Page<TransactionPost> getCreatedAtDescPostsByPageNTransactionTypeNPriceType(Pageable pageable, TransactionType transactionType, PriceType priceType) {
        try {
            Page<TransactionPost> posts = transactionPostRepository.findAllWithPinnedFirstByTransactionTypeAndPriceType(transactionType, priceType, pageable);
            log.info("Successfully got pageable Transaction Posts order by createdAt Desc, transactionType: {} and priceType: {}", transactionType, priceType);
            return posts;
        } catch (Exception e) {
            log.error("Failed to get pageable Transaction Posts order by createdAt Desc, transactionType: {} and priceType: {}", transactionType, priceType, e);
            throw e;
        }
    }

    @Override
    public Page<TransactionPost> getCreatedAtDescPostsByPageNSubCategory(SubCategory subCategory, Pageable pageable) {
        try {
            Page<TransactionPost> posts = transactionPostRepository.findAllBySubCategoryOrderByUpdatedAtDesc(subCategory, pageable);
            log.info("Successfully got pageable Transaction Posts order by createdAt Desc and subcategory: {}", subCategory);
            return posts;
        } catch (Exception e) {
            log.error("Failed to get pageable Transaction Posts order by createdAt Des cand subcategory: {}", subCategory, e);
            throw e;
        }
    }

    @Override
    public Page<TransactionPost> getCreatedAtDescPostsByPageNSubCategoryNTransactionType(SubCategory subCategory, TransactionType transactionType, Pageable pageable) {
        try {
            Page<TransactionPost> posts = transactionPostRepository.findAllBySubCategoryAndTransactionTypeOrderByUpdatedAtDesc(subCategory, transactionType, pageable);
            log.info("Successfully got pageable Transaction Posts order by createdAt Desc, transactionType: {} and subcategory: {}", transactionType, subCategory);
            return posts;
        } catch (Exception e) {
            log.error("Failed to get pageable Transaction Posts order by createdAt Desc, transactionType: {} and subcategory: {}", transactionType, subCategory, e);
            throw e;
        }
    }

    @Override
    public Page<TransactionPost> getCreatedAtDescPostsByPageNSubCategoryNPriceType(SubCategory subCategory, PriceType priceType, Pageable pageable) {
        try {
            Page<TransactionPost> posts = transactionPostRepository.findAllBySubCategoryAndPriceTypeOrderByUpdatedAtDesc(subCategory, priceType, pageable);
            log.info("Successfully got pageable Transaction Posts order by createdAt Desc, priceType: {} and subcategory: {}", priceType, subCategory);
            return posts;
        } catch (Exception e) {
            log.error("Failed to get pageable Transaction Posts order by createdAt Desc, priceType: {} and subcategory: {}", priceType, subCategory, e);
            throw e;
        }
    }

    @Override
    public Page<TransactionPost> getCreatedAtDescPostsByPageNSubCategoryNTransactionTypeNPriceType(SubCategory subCategory, TransactionType transactionType, PriceType priceType, Pageable pageable) {
        try {
            Page<TransactionPost> posts = transactionPostRepository.findAllBySubCategoryAndTransactionTypeAndPriceTypeOrderByUpdatedAtDesc(subCategory, transactionType, priceType, pageable);
            log.info("Successfully got pageable Transaction Posts order by createdAt Desc, transactionType: {}, priceType: {} and subcategory: {}", transactionType, priceType, subCategory);
            return posts;
        } catch (Exception e) {
            log.error("Failed to get pageable Transaction Posts order by createdAt Desc, transactionType: {}, priceType: {} and subcategory: {}", transactionType, priceType, subCategory, e);
            throw e;
        }
    }

    @Override
    public List<TransactionPost> getRecent5Posts() {
        try {
            List<TransactionPost> posts = transactionPostRepository.findTop5ByOrderByUpdatedAtDesc();
            log.info("Successfully got Recent 5 Transaction Posts");
            return posts;
        } catch (Exception e) {
            log.error("Failed to get Recent 5 Transaction Posts", e);
            throw e;
        }
    }

    @Override
    public TransactionPost getPostById(Long id) {
        try {
            Optional<TransactionPost> t = transactionPostRepository.findById(id);
            if (t.isPresent()) {
                log.info("Successfully found transaction post with id: {}", id);
                return t.get();
            }
            throw new TransactionPostNotFoundException("Transaction Post not found with id: " + id);
        } catch (Exception e) {
            log.error("Failed to find transaction post with id: {}", id, e);
            throw e;
        }
    }

    @Override
    public Post createPost(TransactionPostDto transactionPostDto, MultipartFile[] images) {
        User user = userService.getUserById(transactionPostDto.getUserId());
        try {
            TransactionPost transactionPost = TransactionPost.builder()
                    .title(transactionPostDto.getTitle())
                    .description(transactionPostDto.getDescription())
                    .category(Category.사고팔기)
                    .subCategory(transactionPostDto.getSubCategory())
                    .postType(PostType.NORMAL)
                    .contact(transactionPostDto.getContact())
                    .email(transactionPostDto.getEmail())
                    .transactionType(transactionPostDto.getTransactionType())
                    .priceType(transactionPostDto.getPriceType())
                    .price(transactionPostDto.getPrice())
                    .suburb(transactionPostDto.getSuburb())
                    .isCommentAllowed(transactionPostDto.getIsCommentAllowed())
                    .viewCounts(0L)
                    .build();


            transactionPost.setUser(user);
            TransactionPost createdPost = transactionPostRepository.save(transactionPost);

            // save post images data
            if (images != null) {
                Arrays.stream(images)
                        .map(image -> awsFileService.uploadPostFile(image, user.getEmail()))
                        .forEach(imageUrl -> imageService.createImage(imageUrl, createdPost));
            }

            // save keywords
            transactionPostDto.getSelectedKeywords().stream().forEach(
                    keyword -> keywordService.createKeyword(keyword, createdPost)
            );

            log.info("Successfully created transaction post");

            return createdPost;
        } catch (Exception e) {
            log.error("Failed to create transaction post", e);
            return null;
        }
    }

    @Override
    public Post updatePost(UpdateTransactionPostDto updateTransactionPostDto, MultipartFile[] images) {
        UpdateTransactionMainInfoPostDto updateTransactionMainInfoPostDto = updateTransactionPostDto.getUpdateTransactionMainInfoPostDto();
        User user = userService.getUserById(updateTransactionMainInfoPostDto.getUserId());
        TransactionPost transactionPost = getPostById(updateTransactionMainInfoPostDto.getPostId());
        try {
            if (!transactionPost.getTitle().equals(updateTransactionMainInfoPostDto.getTitle())) {
                transactionPost.setTitle(updateTransactionMainInfoPostDto.getTitle());
            }
            if (!Objects.equals(transactionPost.getDescription(), updateTransactionMainInfoPostDto.getDescription())) {
                transactionPost.setDescription(updateTransactionMainInfoPostDto.getDescription());
            }
            if (!Objects.equals(transactionPost.getContact(), updateTransactionMainInfoPostDto.getContact())) {
                transactionPost.setContact(updateTransactionMainInfoPostDto.getContact());
            }
            if (!Objects.equals(transactionPost.getEmail(), updateTransactionMainInfoPostDto.getEmail())) {
                transactionPost.setEmail(updateTransactionMainInfoPostDto.getEmail());
            }
            if (!Objects.equals(transactionPost.getSuburb(), updateTransactionMainInfoPostDto.getSuburb())) {
                transactionPost.setSuburb(updateTransactionMainInfoPostDto.getSuburb());
            }
            if (!Objects.equals(transactionPost.getTransactionType(), updateTransactionMainInfoPostDto.getTransactionType())) {
                transactionPost.setTransactionType(updateTransactionMainInfoPostDto.getTransactionType());
            }
            if (!Objects.equals(transactionPost.getPriceType(), updateTransactionMainInfoPostDto.getPriceType())) {
                transactionPost.setPriceType(updateTransactionMainInfoPostDto.getPriceType());
            }
            if (transactionPost.getPrice() != updateTransactionMainInfoPostDto.getPrice()) {
                transactionPost.setPrice(updateTransactionMainInfoPostDto.getPrice());
            }
            if (!transactionPost.getIsCommentAllowed().equals(updateTransactionMainInfoPostDto.getIsCommentAllowed())) {
                transactionPost.setIsCommentAllowed(updateTransactionMainInfoPostDto.getIsCommentAllowed());
            }

            List<String> imageUrls = transactionPost.getImages().stream().map(Image::getUrl).collect(Collectors.toList());
            List<String> updatedExistingImageUrls = updateTransactionPostDto.getUpdateTransactionMediaInfoPostDto().getExistingImages();
            if (!imageUrls.equals(updatedExistingImageUrls)) {
                List<String> removedImageUrls = imageUrls.stream()
                        .filter(imageUrl -> !updatedExistingImageUrls.contains(imageUrl))
                        .collect(Collectors.toList());

                if (!removedImageUrls.isEmpty()) {
                    transactionPost.getImages().removeIf(image -> removedImageUrls.contains(image.getUrl()));
                }
            }

            // 키워드 업데이트
            List<String> updatedKeywords = updateTransactionMainInfoPostDto.getSelectedKeywords();
            keywordService.updateKeyword(transactionPost, updatedKeywords);

            final ZoneId SYDNEY_ZONE = ZoneId.of("Australia/Sydney");
            transactionPost.setUpdatedAt(Timestamp.from(java.time.ZonedDateTime.now(SYDNEY_ZONE).toInstant()));
            transactionPostRepository.save(transactionPost);
            transactionPostRepository.flush(); // 업데이트 내용 반영

            // save post images data
            if (images != null) {
                TransactionPost createdPost = getPostById(updateTransactionMainInfoPostDto.getPostId());
                Arrays.stream(images)
                        .map(image -> awsFileService.uploadPostFile(image, user.getEmail()))
                        .forEach(imageUrl -> imageService.createImage(imageUrl, createdPost));
            }

            log.info("Successfully updated transaction post");

            return transactionPost;
        } catch (Exception e) {
            log.error("Failed to update transaction post", e);
            return null;
        }
    }

    @Override
    public List<TransactionPost> searchTransactionPost(String title, String subCategory, String suburb, List<String> keywords) {
        boolean isTitleEmpty = title == null || title.equals("");
        boolean isSubCategoryEmpty = subCategory == null || subCategory.equals("");
        boolean isSuburbEmpty = suburb == null || suburb.equals("");

        List<TransactionPost> transactionPostList;
        if (isTitleEmpty && isSubCategoryEmpty && isSuburbEmpty) transactionPostList = searchByCategory();
        else if (isSubCategoryEmpty && isSuburbEmpty) transactionPostList = searchByTitle(title);
        else if (isTitleEmpty && isSuburbEmpty) transactionPostList = searchBySubCategory(SubCategory.valueOf(subCategory));
        else if (isTitleEmpty && isSubCategoryEmpty) transactionPostList = searchBySuburb(Suburb.valueOf(suburb));
        else if (isSuburbEmpty) transactionPostList = searchByTitleAndSubCategory(title, SubCategory.valueOf(subCategory));
        else if (isTitleEmpty) transactionPostList = searchBySubCategoryAndSuburb(SubCategory.valueOf(subCategory), Suburb.valueOf(suburb));
        else if (isSubCategoryEmpty) transactionPostList = searchByTitleAndSuburb(title, Suburb.valueOf(suburb));
        else transactionPostList = searchByTitleAndSubCategoryAndSuburb(title, SubCategory.valueOf(subCategory), Suburb.valueOf(suburb));

        if (keywords != null && !keywords.isEmpty()) {
            transactionPostList = transactionPostList.stream()
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

        return transactionPostList;
    }

    @Override
    public List<TransactionPost> searchByCategory() {
        return transactionPostRepository.findAllByOrderByUpdatedAtDesc();
    }

    @Override
    public List<TransactionPost> searchByTitle(String title) {
        return transactionPostRepository.findByTitleContainingOrderByUpdatedAtDesc(title);
    }

    @Override
    public List<TransactionPost> searchBySubCategory(SubCategory subCategory) {
        return transactionPostRepository.findBySubCategoryOrderByUpdatedAtDesc(subCategory);
    }

    @Override
    public List<TransactionPost> searchBySuburb(Suburb suburb) {
        return transactionPostRepository.findBySuburbOrderByUpdatedAtDesc(suburb);
    }

    @Override
    public List<TransactionPost> searchByTitleAndSubCategory(String title, SubCategory subCategory) {
        return transactionPostRepository.findByTitleContainingAndSubCategoryOrderByUpdatedAtDesc(title, subCategory);
    }

    @Override
    public List<TransactionPost> searchByTitleAndSuburb(String title, Suburb suburb) {
        return transactionPostRepository.findByTitleContainingAndSuburbOrderByUpdatedAtDesc(title, suburb);
    }

    @Override
    public List<TransactionPost> searchBySubCategoryAndSuburb(SubCategory subCategory, Suburb suburb) {
        return transactionPostRepository.findBySubCategoryAndSuburbOrderByUpdatedAtDesc(subCategory, suburb);
    }

    @Override
    public List<TransactionPost> searchByTitleAndSubCategoryAndSuburb(String title, SubCategory subCategory, Suburb suburb) {
        return transactionPostRepository.findByTitleContainingAndSubCategoryAndSuburbOrderByUpdatedAtDesc(title, subCategory, suburb);
    }

    @Override
    public Page<TransactionPost> convertPostsAsPage(List<TransactionPost> posts, Pageable pageable) {
        try {
            int start = (int) pageable.getOffset();
            int end = Math.min(start + pageable.getPageSize(), posts.size());

            List<TransactionPost> paginatedPosts = posts.subList(start, end);

            return new PageImpl<>(paginatedPosts, pageable, posts.size());
        } catch (Exception e) {
            log.error("Failed to convert Post as Page");
            throw e;
        }
    }
}
