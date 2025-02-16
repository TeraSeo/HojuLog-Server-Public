package com.hojunara.web.service;

import com.hojunara.web.entity.Post;
import java.util.List;

import com.hojunara.web.entity.SubCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {
    List<Post> getWholePosts();

    Page<Post> getPostsByPageNCondition(Pageable pageable, String condition);

    Page<Post> getPostsByPageNUser(Long userId, Pageable pageable);

    Page<Post> getPostsByPageNLiked(Long userId, Pageable pageable);

    Post getPostById(Long id);

    void addViewCount(Long postId, String userId);

    Page<Post> getPostsAsPage(List<Post> posts, Pageable pageable);

    Boolean removePost(Long postId);
}
