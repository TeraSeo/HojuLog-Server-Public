package com.hojunara.web.service;

import com.hojunara.web.aws.s3.AwsFileService;
import com.hojunara.web.dto.request.ArticlePostDto;
import com.hojunara.web.dto.request.UpdateArticleMainInfoPostDto;
import com.hojunara.web.dto.request.UpdateArticlePostDto;
import com.hojunara.web.dto.request.UpdateTransactionMainInfoPostDto;
import com.hojunara.web.entity.*;
import com.hojunara.web.exception.ArticlePostNotFoundException;
import com.hojunara.web.repository.ArticlePostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ArticlePostServiceImpl implements ArticlePostService {

    private final ArticlePostRepository articlePostRepository;
    private final UserService userService;
    private final AwsFileService awsFileService;
    private final ArticleImageService articleImageService;

    @Autowired
    public ArticlePostServiceImpl(ArticlePostRepository articlePostRepository, UserService userService, AwsFileService awsFileService, ArticleImageService articleImageService) {
        this.articlePostRepository = articlePostRepository;
        this.userService = userService;
        this.awsFileService = awsFileService;
        this.articleImageService = articleImageService;
    }

    @Override
    public ArticlePost getPostById(Long postId) {
        try {
            Optional<ArticlePost> a = articlePostRepository.findById(postId);
            if (a.isPresent()) {
                log.info("Successfully found article post with id: {}", postId);
                return a.get();
            }
            throw new ArticlePostNotFoundException("Article Post not found with id: " + postId);
        } catch (Exception e) {
            log.error("Failed to find article post with id: {}", postId, e);
            throw e;
        }
    }

    @Override
    public List<ArticlePost> getWholePosts() {
        try {
            List<ArticlePost> posts = articlePostRepository.findAll();
            log.info("Successfully got Whole Article Posts");
            return posts;
        } catch (Exception e) {
            log.error("Failed to get Whole Article Posts", e);
            throw e;
        }
    }

    @Override
    public Page<ArticlePost> getCreatedAtDescPostsByPage(Pageable pageable) {
        try {
            Page<ArticlePost> posts = articlePostRepository.findAllByOrderByUpdatedAtDesc(pageable);
            log.info("Successfully got pageable Article Posts order by createdAt Desc");
            return posts;
        } catch (Exception e) {
            log.error("Failed to get pageable Article Posts order by createdAt Desc", e);
            throw e;
        }
    }

    @Override
    public ArticlePost createArticle(ArticlePostDto articlePostDto, MultipartFile[] images) {
        User user = userService.getUserById(articlePostDto.getUserId());
        try {
            ArticlePost articlePost = ArticlePost.builder()
                    .title(articlePostDto.getTitle())
                    .description(articlePostDto.getDescription())
                    .category(Category.게시판)
                    .subCategory(SubCategory.게시판)
                    .postType(PostType.ARTICLE)
                    .isCommentAllowed(articlePostDto.getIsCommentAllowed())
                    .viewCounts(0L)
                    .build();

            articlePost.setUser(user);
            ArticlePost createdPost = articlePostRepository.save(articlePost);

            // save post images data
            if (images != null) {
                Arrays.stream(images)
                        .map(image -> awsFileService.uploadPostFile(image, user.getEmail()))
                        .forEach(imageUrl -> articleImageService.createImage(imageUrl, createdPost));
            }

            log.info("Successfully created article post");

            return createdPost;
        } catch (Exception e) {
            log.error("Failed to create article post", e);
            return null;
        }
    }

    @Override
    public Post updateArticle(UpdateArticlePostDto updateArticlePostDto, MultipartFile[] images) {
        UpdateArticleMainInfoPostDto updateArticleMainInfoPostDto = updateArticlePostDto.getUpdateArticleMainInfoPostDto();
        User user = userService.getUserById(updateArticleMainInfoPostDto.getUserId());
        ArticlePost articlePost = getPostById(updateArticleMainInfoPostDto.getPostId());
        try {
            if (!articlePost.getTitle().equals(updateArticleMainInfoPostDto.getTitle())) {
                articlePost.setTitle(updateArticleMainInfoPostDto.getTitle());
            }
            if (!Objects.equals(articlePost.getDescription(), updateArticleMainInfoPostDto.getDescription())) {
                articlePost.setDescription(updateArticleMainInfoPostDto.getDescription());
            }
            if (!articlePost.getIsCommentAllowed().equals(updateArticleMainInfoPostDto.getIsCommentAllowed())) {
                articlePost.setIsCommentAllowed(updateArticleMainInfoPostDto.getIsCommentAllowed());
            }

            List<String> imageUrls = articlePost.getImages().stream().map(ArticleImage::getUrl).collect(Collectors.toList());
            List<String> updatedExistingImageUrls = updateArticlePostDto.getUpdateArticleMediaInfoPostDto().getExistingImages();
            if (!imageUrls.equals(updatedExistingImageUrls)) {
                List<String> removedImageUrls = imageUrls.stream()
                        .filter(imageUrl -> !updatedExistingImageUrls.contains(imageUrl))
                        .collect(Collectors.toList());

                if (!removedImageUrls.isEmpty()) {
                    articlePost.getImages().removeIf(image -> removedImageUrls.contains(image.getUrl()));
                }
            }

            final ZoneId SYDNEY_ZONE = ZoneId.of("Australia/Sydney");
            articlePost.setUpdatedAt(Timestamp.from(java.time.ZonedDateTime.now(SYDNEY_ZONE).toInstant()));
            articlePostRepository.save(articlePost);
            articlePostRepository.flush(); // 업데이트 내용 반영

            // save post images data
            if (images != null) {
                ArticlePost createdPost = getPostById(updateArticleMainInfoPostDto.getPostId());
                Arrays.stream(images)
                        .map(image -> awsFileService.uploadPostFile(image, user.getEmail()))
                        .forEach(imageUrl -> articleImageService.createImage(imageUrl, createdPost));
            }

            log.info("Successfully updated article post");

            return articlePost;
        } catch (Exception e) {
            log.error("Failed to update article post", e);
            return null;
        }
    }

    @Override
    public Page<ArticlePost> getAllPostsByPageNUser(Long userId, Pageable pageable) {
        try {
            Page<ArticlePost> articlePosts = articlePostRepository.findAllByUserId(userId, pageable);
            log.info("Successfully found all article posts by page and user");
            return articlePosts;
        } catch (Exception e) {
            log.error("Failed to find all article posts by page", e);
            throw e;
        }
    }

    @Override
    public List<ArticlePost> getTop5PostsByUser(Long userId) {
        try {
            List<ArticlePost> articlePosts = articlePostRepository.findTop5ByUserIdOrderByCreatedAtDesc(userId);
            log.info("Successfully found top 5 article posts by userId: {}", userId);
            return articlePosts;
        } catch (Exception e) {
            log.error("Failed to find top 5 article posts by userId: {}", userId, e);
            throw e;
        }
    }
}
