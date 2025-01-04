package com.hojunara.web.service;

import com.hojunara.web.entity.Post;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {
    List<Post> getWholePosts();

    Page<Post> getPostsByPageNCondition(Pageable pageable, String condition);

    Post getPostById(Long id);

    void addViewCount(Long id);
}
