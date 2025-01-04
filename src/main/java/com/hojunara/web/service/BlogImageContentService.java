package com.hojunara.web.service;

import com.hojunara.web.entity.BlogPost;
import com.hojunara.web.entity.ImageContent;

public interface BlogImageContentService {
    void createBlogImageContent(ImageContent imageContent, BlogPost blogPost);
}
