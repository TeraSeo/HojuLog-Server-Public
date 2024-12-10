package com.hojunara.web.service;

import com.hojunara.web.entity.Comment;
import com.hojunara.web.entity.CommentLike;
import com.hojunara.web.entity.User;

import java.util.List;

public interface CommentLikeService {
    List<CommentLike> getWholeLikesByCommentId(Long id);

    CommentLike getCommentLikeById(Long id);

    Long createLike(Comment comment, User user);

    Long deleteCommentLikeById(Long commentId, Long userId);
}
