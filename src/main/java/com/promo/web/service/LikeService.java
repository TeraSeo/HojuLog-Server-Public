package com.promo.web.service;

import com.promo.web.entity.Post;
import com.promo.web.entity.PostLike;
import com.promo.web.entity.User;

import java.util.List;
import java.util.Optional;

public interface LikeService {
    List<PostLike> getWholeLikesByPostId(Long id);

    PostLike getPostLikeById(Long id);

    void createLike(Post post, User user);

    void deletePostLikeById(Long id);
}
