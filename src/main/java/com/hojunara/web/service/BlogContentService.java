package com.hojunara.web.service;

import com.hojunara.web.entity.BlogContent;
import com.hojunara.web.entity.BlogPost;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Service interface for managing {@link BlogContent} entities.
 * <p>
 * Provides methods for retrieving, creating, updating, and saving {@link BlogContent} data.
 * </p>
 *
 * @author Taejun Seo
 */
public interface BlogContentService {
    BlogContent getBlogContentById(Long contentId);

    void createBlogContent(BlogContent blogContent, BlogPost blogPost);

    void updateBlogContent(BlogContent blogContent);

    void saveBlogContentList(List<BlogContent> blogContents, MultipartFile[] images, String email, BlogPost blogPost);

    void updateBlogContentList(List<BlogContent> blogContents, List<Long> orderList, MultipartFile[] images, String email, BlogPost blogPost);
}
