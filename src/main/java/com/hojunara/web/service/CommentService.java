package com.hojunara.web.service;

import com.hojunara.web.entity.Comment;
import com.hojunara.web.entity.Post;
import com.hojunara.web.entity.User;

import java.util.List;

public interface CommentService {
    List<Comment> getWholeCommentsByPostId(Long id);

    Comment getCommentById(Long id);

    Comment createComment(Post post, User user, String content);

    void deleteCommentById(Long id);
}
