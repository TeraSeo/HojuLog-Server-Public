package com.hojunara.web.service;

import com.hojunara.web.aws.s3.AwsFileService;
import com.hojunara.web.dto.request.TravelPostDto;
import com.hojunara.web.entity.*;
import com.hojunara.web.exception.TravelPostNotFoundException;
import com.hojunara.web.repository.TravelPostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class TravelPostServiceImpl implements TravelPostService {

    private final TravelPostRepository travelPostRepository;
    private final UserService userService;
    private final AwsFileService awsFileService;
    private final BlogDescriptionContentService blogDescriptionContentService;
    private final BlogImageContentService blogImageContentService;

    @Autowired
    public TravelPostServiceImpl(TravelPostRepository travelPostRepository, UserService userService, AwsFileService awsFileService, BlogDescriptionContentService blogDescriptionContentService, BlogImageContentService blogImageContentService) {
        this.travelPostRepository = travelPostRepository;
        this.userService = userService;
        this.awsFileService = awsFileService;
        this.blogDescriptionContentService = blogDescriptionContentService;
        this.blogImageContentService = blogImageContentService;
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
    public Page<TravelPost> getCreatedAtDescPostsByPage(Pageable pageable) {
        try {
            Page<TravelPost> posts = travelPostRepository.findAllByOrderByCreatedAtDesc(pageable);
            log.info("Successfully got pageable Travel Posts order by createdAt Desc");
            return posts;
        } catch (Exception e) {
            log.error("Failed to get pageable Travel Posts order by createdAt Desc", e);
            throw e;
        }
    }

    @Override
    public Page<TravelPost> getCreatedAtDescPostsByPageNSubCategory(SubCategory subCategory, Pageable pageable) {
        try {
            Page<TravelPost> posts = travelPostRepository.findAllBySubCategoryOrderByCreatedAtDesc(subCategory, pageable);
            log.info("Successfully got pageable Travel Posts order by createdAt Desc and subcategory: {}", subCategory);
            return posts;
        } catch (Exception e) {
            log.error("Failed to get pageable Travel Posts order by createdAt Desc and subcategory: {}", subCategory, e);
            throw e;
        }
    }

    @Override
    public List<TravelPost> getRecent5Posts() {
        try {
            List<TravelPost> posts = travelPostRepository.findTop5ByOrderByCreatedAtDesc();
            log.info("Successfully got Recent 5 Travel Posts");
            return posts;
        } catch (Exception e) {
            log.error("Failed to get Recent 5 Travel Posts", e);
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
                    .category(Category.여행)
                    .subCategory(travelPostDto.getSubCategory())
                    .postType(PostType.BLOG)
                    .country(travelPostDto.getCountry())
                    .location(travelPostDto.getLocation())
                    .rate(travelPostDto.getRate())
                    .build();

            travelPost.setUser(user);
            TravelPost createdPost = travelPostRepository.save(travelPost);

            // save post images data
            if (travelPostDto.getBlogContents().size() > 0) {
                List<BlogContent> blogContents = BlogContent.convertMapToBlogContent(travelPostDto.getBlogContents());
                int imgCnt = 0;
                for (int i = 0; i < blogContents.size(); i++) {
                    BlogContent blogContent = blogContents.get(i);
                    if (blogContent.getType().toString().equals("image")) {
                        if (imgCnt < images.length) {
                            String imgUrl = awsFileService.uploadPostFile(images[imgCnt], user.getEmail());
                            ImageContent imageContent = (ImageContent) blogContent;
                            imageContent.setImageUrl(imgUrl);
                            blogImageContentService.createBlogImageContent(imageContent, travelPost);
                            imgCnt++;
                        }
                    }
                    else {
                        DescriptionContent descriptionContent = (DescriptionContent) blogContent;
                        blogDescriptionContentService.createBlogDescriptionContent(descriptionContent, travelPost);
                    }
                }
            }

            log.info("Successfully created travel post");

            return createdPost;
        } catch (Exception e) {
            log.error("Failed to create travel post", e);
            return null;
        }
    }
}
