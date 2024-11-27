package com.promo.web.service;

import com.promo.web.entity.Image;
import com.promo.web.entity.Post;
import com.promo.web.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PostService {
    List<Post> getWholePosts();

    Post getPostById(Long id);

//    void createPost(Long userId, Post post);
//
//    void createPostWithTagsNAdditionalUrls(User user, Post post, Set<String> tagNames, List<String> urls);
//
//    void updatePost(Long id, Post post, Set<String> newTagNames, List<String> newUrls, List<Image> images);
//
//    void deletePostById(Long id);
}
