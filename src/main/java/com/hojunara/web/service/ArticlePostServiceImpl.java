package com.hojunara.web.service;

import com.hojunara.web.aws.s3.AwsFileService;
import com.hojunara.web.dto.request.ArticlePostDto;
import com.hojunara.web.dto.request.UpdateArticleMainInfoPostDto;
import com.hojunara.web.dto.request.UpdateArticlePostDto;
import com.hojunara.web.entity.*;
import com.hojunara.web.exception.EntityNotFoundException;
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
import java.util.stream.Collectors;

/**
 * Implementation of the {@link ArticlePostService} interface for managing {@link ArticlePost} data.
 * <p>
 * Provides methods for retrieving, creating, updating, and paginating {@link ArticlePost} data.
 * </p>
 *
 * @author Taejun Seo
 */
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

    /**
     * Retrieves an {@link ArticlePost} by its ID.
     *
     * @param postId the ID of the {@link ArticlePost} to retrieve
     * @return the {@link ArticlePost} associated with the given ID
     * @throws EntityNotFoundException if no {@link ArticlePost} is found with the provided ID
     */
    @Override
    public ArticlePost getPostById(Long postId) {
        return articlePostRepository.findById(postId)
            .orElseThrow(() -> new EntityNotFoundException("ArticlePost Not Found Exception", postId));
    }

    /**
     * Retrieves all {@link ArticlePost} data.
     *
     * @return a list of all {@link ArticlePost} entities
     */
    @Override
    public List<ArticlePost> getWholePosts() {
        List<ArticlePost> posts = articlePostRepository.findAll();
        log.info("Successfully got Whole Article Posts");
        return posts;
    }

    /**
     * Retrieves {@link ArticlePost} data ordered by creation date in descending order, paginated.
     *
     * @param pageable the pagination information
     * @return a {@link Page} of {@link ArticlePost} data
     */
    @Override
    public Page<ArticlePost> getCreatedAtDescPostsByPage(Pageable pageable) {
        Page<ArticlePost> posts = articlePostRepository.findAllByOrderByUpdatedAtDesc(pageable);
        log.info("Successfully got pageable Article Posts order by createdAt Desc");
        return posts;
    }

    /**
     * Creates a new {@link ArticlePost} with the provided details and images.
     *
     * @param articlePostDto the data transfer object containing {@link ArticlePost} details
     * @param images the images to associate with the {@link ArticlePost}
     * @return the created {@link ArticlePost} entity
     */
    @Override
    public ArticlePost createArticle(ArticlePostDto articlePostDto, MultipartFile[] images) {
        User user = userService.getUserById(articlePostDto.getUserId());
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
    }

    /**
     * Updates an existing {@link ArticlePost} with the provided details and images.
     *
     * @param updateArticlePostDto the data transfer object containing updated {@link ArticlePost} details
     * @param images the images to associate with the {@link ArticlePost}
     * @return the updated {@link ArticlePost} entity
     */
    @Override
    public Post updateArticle(UpdateArticlePostDto updateArticlePostDto, MultipartFile[] images) {
        UpdateArticleMainInfoPostDto updateArticleMainInfoPostDto = updateArticlePostDto.getUpdateArticleMainInfoPostDto();
        User user = userService.getUserById(updateArticleMainInfoPostDto.getUserId());
        ArticlePost articlePost = getPostById(updateArticleMainInfoPostDto.getPostId());

        if (!Objects.equals(articlePost.getTitle(), updateArticleMainInfoPostDto.getTitle())) {
            articlePost.setTitle(updateArticleMainInfoPostDto.getTitle());
        }
        if (!Objects.equals(articlePost.getDescription(), updateArticleMainInfoPostDto.getDescription())) {
            articlePost.setDescription(updateArticleMainInfoPostDto.getDescription());
        }
        if (!Objects.equals(articlePost.getIsCommentAllowed(), updateArticleMainInfoPostDto.getIsCommentAllowed())) {
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
        articlePostRepository.flush(); // apply the updated changes

        // save post images data
        if (images != null) {
            ArticlePost createdPost = getPostById(updateArticleMainInfoPostDto.getPostId());
            Arrays.stream(images)
                    .map(image -> awsFileService.uploadPostFile(image, user.getEmail()))
                    .forEach(imageUrl -> articleImageService.createImage(imageUrl, createdPost));
        }

        log.info("Successfully updated article post");

        return articlePost;
    }

    /**
     * Retrieves {@link ArticlePost} data for a specific user, paginated.
     *
     * @param userId the ID of the user whose posts to retrieve
     * @param pageable the pagination information
     * @return a {@link Page} of {@link ArticlePost} data for the specified user
     */
    @Override
    public Page<ArticlePost> getAllPostsByPageNUser(Long userId, Pageable pageable) {
        Page<ArticlePost> articlePosts = articlePostRepository.findAllByUserId(userId, pageable);
        log.info("Successfully found all article posts by page and user");
        return articlePosts;
    }

    /**
     * Retrieves the top 5 {@link ArticlePost} data by a specific user.
     *
     * @param userId the ID of the user whose top 5 posts to retrieve
     * @return a list of the top 5 {@link ArticlePost} entities for the specified user
     */
    @Override
    public List<ArticlePost> getTop5PostsByUser(Long userId) {
        List<ArticlePost> articlePosts = articlePostRepository.findTop5ByUserIdOrderByCreatedAtDesc(userId);
        log.info("Successfully found top 5 article posts by userId: {}", userId);
        return articlePosts;
    }
}