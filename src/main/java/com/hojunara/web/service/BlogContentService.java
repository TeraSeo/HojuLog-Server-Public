package com.hojunara.web.service;

import com.hojunara.web.entity.BlogContent;
import com.hojunara.web.entity.BlogPost;

public interface BlogContentService {
    BlogContent getBlogContentById(Long id);

    void createBlogContent(BlogContent blogContent, BlogPost blogPost);
}
