package com.hojunara.web.service;

import com.hojunara.web.aws.s3.AwsFileService;
import com.hojunara.web.entity.BlogContent;
import com.hojunara.web.entity.BlogPost;
import com.hojunara.web.entity.DescriptionContent;
import com.hojunara.web.entity.ImageContent;
import com.hojunara.web.exception.EntityNotFoundException;
import com.hojunara.web.repository.BlogContentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Implementation of the {@link BlogContentService} interface for managing {@link BlogContent} data.
 * <p>
 * Provides methods for creating, updating, and saving {@link BlogContent}, handling both image and description types.
 * </p>
 *
 * @author Taejun Seo
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class BlogContentServiceImpl implements BlogContentService {

    private final BlogContentRepository blogContentRepository;
    private final AwsFileService awsFileService;
    private final BlogImageContentService blogImageContentService;
    private final BlogDescriptionContentService blogDescriptionContentService;

    @Autowired
    public BlogContentServiceImpl(BlogContentRepository blogContentRepository, AwsFileService awsFileService, BlogImageContentService blogImageContentService, BlogDescriptionContentService blogDescriptionContentService) {
        this.blogContentRepository = blogContentRepository;
        this.awsFileService = awsFileService;
        this.blogImageContentService = blogImageContentService;
        this.blogDescriptionContentService = blogDescriptionContentService;
    }

    /**
     * Retrieves a {@link BlogContent} by its ID.
     *
     * @param contentId the ID of the {@link BlogContent} to retrieve
     * @return the {@link BlogContent} associated with the given ID
     * @throws EntityNotFoundException if no {@link BlogContent} is found with the provided ID
     */
    @Override
    public BlogContent getBlogContentById(Long contentId) {
        return blogContentRepository.findById(contentId)
            .orElseThrow(() -> new EntityNotFoundException("BlogContent Not Found Exception", contentId));
    }

    /**
     * Creates a new {@link BlogContent} and associates it with the provided {@link BlogPost}.
     *
     * @param blogContent the {@link BlogContent} to be created
     * @param blogPost the {@link BlogPost} that the {@link BlogContent} will be associated with
     */
    @Override
    public void createBlogContent(BlogContent blogContent, BlogPost blogPost) {
        blogContent.setPost(blogPost);
        blogPost.getBlogContents().add(blogContent);
        blogContentRepository.save(blogContent);
        log.info("Successfully created blog content");
    }

    /**
     * Updates an existing {@link BlogContent}.
     *
     * @param blogContent the {@link BlogContent} to be updated
     */
    @Override
    public void updateBlogContent(BlogContent blogContent) {
        blogContentRepository.save(blogContent);
        log.info("Successfully updated blog content");
    }

    /**
     * Saves a list of {@link BlogContent} entities, associates them with the provided {@link BlogPost},
     * uploads images, and associates image content with the blog post.
     *
     * @param blogContents the list of {@link BlogContent} to be saved
     * @param images the images to be uploaded and associated with the {@link BlogContent}
     * @param email the email of the user creating the content
     * @param blogPost the {@link BlogPost} to associate the content with
     */
    @Override
    public void saveBlogContentList(List<BlogContent> blogContents, MultipartFile[] images, String email, BlogPost blogPost) {
        int imgCnt = 0;
        for (int i = 0; i < blogContents.size(); i++) {
            BlogContent blogContent = blogContents.get(i);
            blogContent.setOrderIndex((long) i);
            if (blogContent.getType().toString().equals("image")) {
                ImageContent imageContent = (ImageContent) blogContent;
                if (imageContent.getImageUrl().equals(images[imgCnt].getOriginalFilename())) {
                    if (imgCnt < images.length) {
                        String imgUrl = awsFileService.uploadPostFile(images[imgCnt], email);
                        imageContent.setImageUrl(imgUrl);
                        blogImageContentService.createBlogImageContent(imageContent, blogPost);
                        imgCnt++;
                    }
                }
            }
            else {
                DescriptionContent descriptionContent = (DescriptionContent) blogContent;
                blogDescriptionContentService.createBlogDescriptionContent(descriptionContent, blogPost);
            }
        }
    }

    /**
     * Updates a list of {@link BlogContent} entities, reorders them, uploads new images, and saves the changes.
     * Associates content with the provided {@link BlogPost}.
     *
     * @param blogContents the list of {@link BlogContent} to be updated
     * @param orderList the new order of the {@link BlogContent} entities
     * @param images the images to be uploaded and associated with the {@link BlogContent}
     * @param email the email of the user updating the content
     * @param blogPost the {@link BlogPost} to associate the content with
     */
    @Override
    public void updateBlogContentList(List<BlogContent> blogContents, List<Long> orderList, MultipartFile[] images, String email, BlogPost blogPost) {
        int imgCnt = 0;
        for (int i = 0; i < blogContents.size(); i++) {
            BlogContent blogContent = blogContents.get(i);
            blogContent.setOrderIndex(orderList.get(i));
            if (blogContent.getType().toString().equals("image")) {
                ImageContent imageContent = (ImageContent) blogContent;
                if (imageContent.getImageUrl().equals(images[imgCnt].getOriginalFilename())) {
                    if (imgCnt < images.length) {
                        String imgUrl = awsFileService.uploadPostFile(images[imgCnt], email);
                        imageContent.setImageUrl(imgUrl);
                        blogImageContentService.createBlogImageContent(imageContent, blogPost);
                        imgCnt++;
                    }
                }
            }
            else {
                DescriptionContent descriptionContent = (DescriptionContent) blogContent;
                blogDescriptionContentService.createBlogDescriptionContent(descriptionContent, blogPost);
            }
        }
    }
}
