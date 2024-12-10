package com.promo.web.service;

import com.promo.web.entity.Post;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {
    List<Post> getWholePosts();

    Page<Post> getPostsByPageNCondition(Pageable pageable, String condition);

    Post getPostById(Long id);

//    void createPost(Long userId, Post post);
//
//    void createPostWithTagsNAdditionalUrls(User user, Post post, Set<String> tagNames, List<String> urls);
//
//    void updatePost(Long id, Post post, Set<String> newTagNames, List<String> newUrls, List<Image> images);
//
//    void deletePostById(Long id);
}
