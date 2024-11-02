package com.promo.web.service;

import com.promo.web.entity.Comment;
import com.promo.web.entity.Post;
import com.promo.web.entity.PostLike;
import com.promo.web.entity.User;

import java.util.List;

public interface CommentService {
    List<Comment> getWholeCommentsByPostId(Long id);

    Comment getCommentById(Long id);

    void createComment(Post post, User user, String content);

    void deleteCommentById(Long id);
}
