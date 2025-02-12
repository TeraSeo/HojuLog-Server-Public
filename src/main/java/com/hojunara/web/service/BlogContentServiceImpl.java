package com.hojunara.web.service;

import com.hojunara.web.aws.s3.AwsFileService;
import com.hojunara.web.entity.BlogContent;
import com.hojunara.web.entity.BlogPost;
import com.hojunara.web.entity.DescriptionContent;
import com.hojunara.web.entity.ImageContent;
import com.hojunara.web.exception.BlogContentNotFoundException;
import com.hojunara.web.repository.BlogContentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

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

    @Override
    public BlogContent getBlogContentById(Long id) {
        try {
            Optional<BlogContent> blogContent = blogContentRepository.findById(id);
            if (blogContent.isPresent()) {
                log.info("Successfully got blog content by id: {}", id);
                return blogContent.get();
            }
            throw new BlogContentNotFoundException("Blog content not found with id: " + id);
        } catch (Exception e) {
            log.error("Failed to get blog content by id: {}", id, e);
            throw e;
        }
    }

    @Override
    public void createBlogContent(BlogContent blogContent, BlogPost blogPost) {
        try {
            blogContent.setPost(blogPost);
            blogPost.getBlogContents().add(blogContent);
            blogContentRepository.save(blogContent);
            log.info("Successfully created blog content");
        } catch (Exception e) {
            log.error("Failed to create blog content");
            throw e;
        }
    }

    @Override
    public void updateBlogContent(BlogContent blogContent) {
        try {
            blogContentRepository.save(blogContent);
            log.info("Successfully updated blog content");
        } catch (Exception e) {
            log.error("Failed to update blog content");
            throw e;
        }
    }

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
