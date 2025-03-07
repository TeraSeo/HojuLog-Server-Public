package com.hojunara.web.service;

import com.hojunara.web.entity.BlogPost;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogPostService {
    BlogPost getBlogPostById(Long id);
}
