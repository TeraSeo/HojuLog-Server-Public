package com.promo.web.service;

import com.promo.web.entity.Comment;
import com.promo.web.entity.CommentLike;
import com.promo.web.entity.User;

import java.util.List;

public interface CommentLikeService {
    List<CommentLike> getWholeLikesByCommentId(Long id);

    CommentLike getCommentLikeById(Long id);

    Long createLike(Comment comment, User user);

    Long deleteCommentLikeById(Long commentId, Long userId);
}
