package com.hojunara.web.service;

import com.hojunara.web.entity.BlogPost;
import com.hojunara.web.entity.DescriptionContent;

public interface BlogDescriptionContentService {
    void createBlogDescriptionContent(DescriptionContent descriptionContent, BlogPost blogPost);
}
