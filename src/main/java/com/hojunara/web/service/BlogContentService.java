package com.hojunara.web.service;

import com.hojunara.web.entity.BlogContent;
import com.hojunara.web.entity.BlogPost;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BlogContentService {
    BlogContent getBlogContentById(Long id);

    void createBlogContent(BlogContent blogContent, BlogPost blogPost);

    void updateBlogContent(BlogContent blogContent);

    void saveBlogContentList(List<BlogContent> blogContents, MultipartFile[] images, String email, BlogPost blogPost);

    void updateBlogContentList(List<BlogContent> blogContents, List<Long> orderList, MultipartFile[] images, String email, BlogPost blogPost);
}
