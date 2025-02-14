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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
            Page<TransactionPost> posts = transactionPostRepository.findAllByOrderByCreatedAtDesc(pageable);
            log.info("Successfully got pageable Transaction Posts order by createdAt Desc");
            return posts;
        } catch (Exception e) {
            log.error("Failed to get pageable Transaction Posts order by createdAt Desc", e);
            throw e;
        }
    }

    @Override
    public Page<TransactionPost> getCreatedAtDescPostsByPageNSubCategory(SubCategory subCategory, Pageable pageable) {
        try {
            Page<TransactionPost> posts = transactionPostRepository.findAllBySubCategoryOrderByCreatedAtDesc(subCategory, pageable);
            log.info("Successfully got pageable Transaction Posts order by createdAt Desc and subcategory: {}", subCategory);
            return posts;
        } catch (Exception e) {
            log.error("Failed to get pageable Transaction Posts order by createdAt Des cand subcategory: {}", subCategory, e);
            throw e;
        }
    }

    @Override
    public List<TransactionPost> getRecent5Posts() {
        try {
            List<TransactionPost> posts = transactionPostRepository.findTop5ByOrderByCreatedAtDesc();
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
        try {
            TransactionPost transactionPost = getPostById(updateTransactionMainInfoPostDto.getPostId());

            boolean isUpdated = false;

            if (!transactionPost.getTitle().equals(updateTransactionMainInfoPostDto.getTitle())) {
                transactionPost.setTitle(updateTransactionMainInfoPostDto.getTitle());
                isUpdated = true;
            }
            if (!Objects.equals(transactionPost.getDescription(), updateTransactionMainInfoPostDto.getDescription())) {
                transactionPost.setDescription(updateTransactionMainInfoPostDto.getDescription());
                isUpdated = true;
            }
            if (!Objects.equals(transactionPost.getContact(), updateTransactionMainInfoPostDto.getContact())) {
                transactionPost.setContact(updateTransactionMainInfoPostDto.getContact());
                isUpdated = true;
            }
            if (!Objects.equals(transactionPost.getEmail(), updateTransactionMainInfoPostDto.getEmail())) {
                transactionPost.setEmail(updateTransactionMainInfoPostDto.getEmail());
                isUpdated = true;
            }
            if (!Objects.equals(transactionPost.getSuburb(), updateTransactionMainInfoPostDto.getSuburb())) {
                transactionPost.setSuburb(updateTransactionMainInfoPostDto.getSuburb());
                isUpdated = true;
            }
            if (!Objects.equals(transactionPost.getTransactionType(), updateTransactionMainInfoPostDto.getTransactionType())) {
                transactionPost.setTransactionType(updateTransactionMainInfoPostDto.getTransactionType());
                isUpdated = true;
            }
            if (!Objects.equals(transactionPost.getPriceType(), updateTransactionMainInfoPostDto.getPriceType())) {
                transactionPost.setPriceType(updateTransactionMainInfoPostDto.getPriceType());
                isUpdated = true;
            }
            if (transactionPost.getPrice() != updateTransactionMainInfoPostDto.getPrice()) {
                transactionPost.setPrice(updateTransactionMainInfoPostDto.getPrice());
                isUpdated = true;
            }
            if (!transactionPost.getIsCommentAllowed().equals(updateTransactionMainInfoPostDto.getIsCommentAllowed())) {
                transactionPost.setIsCommentAllowed(updateTransactionMainInfoPostDto.getIsCommentAllowed());
                isUpdated = true;
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
            List<String> originalKeywords = transactionPost.getKeywords().stream().map(Keyword::getKeyWord).collect(Collectors.toList());
            List<String> updatedKeywords = updateTransactionMainInfoPostDto.getSelectedKeywords();
            if (!originalKeywords.equals(updatedKeywords)) {
                List<String> addedKeywords = updatedKeywords.stream()
                        .filter(keyword -> !originalKeywords.contains(keyword))
                        .collect(Collectors.toList());

                List<String> removedKeywords = originalKeywords.stream()
                        .filter(keyword -> !updatedKeywords.contains(keyword))
                        .collect(Collectors.toList());

                if (!addedKeywords.isEmpty()) {
                    addedKeywords.forEach(keyword -> {
                        keywordService.createKeyword(keyword, transactionPost);
                    });
                }

                if (!removedKeywords.isEmpty()) {
                    transactionPost.getKeywords().removeIf(keyword -> removedKeywords.contains(keyword.getKeyWord()));
                }

                isUpdated = true;
            }

            if (isUpdated) {
                transactionPostRepository.save(transactionPost);
                transactionPostRepository.flush(); // 업데이트 내용 반영
            }

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
}
