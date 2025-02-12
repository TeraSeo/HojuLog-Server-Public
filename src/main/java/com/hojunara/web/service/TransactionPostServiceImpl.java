package com.hojunara.web.service;

import com.hojunara.web.aws.s3.AwsFileService;
import com.hojunara.web.dto.request.TransactionPostDto;
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
import java.util.Optional;

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
}
