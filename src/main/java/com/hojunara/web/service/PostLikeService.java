package com.hojunara.web.service;

import com.hojunara.web.entity.Post;
import com.hojunara.web.entity.PostLike;
import com.hojunara.web.entity.User;

import java.util.List;

public interface PostLikeService {
    List<PostLike> getWholeLikesByPostId(Long id);

    PostLike getPostLikeById(Long id);

    Long createLike(Post post, User user);

    Long deletePostLikeById(Long postId, Long userId);

    Boolean checkIsPostLikedByUser(Long userId, Long postId);
}
