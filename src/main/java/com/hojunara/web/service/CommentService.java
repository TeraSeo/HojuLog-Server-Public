package com.hojunara.web.service;

import com.hojunara.web.entity.Comment;

public interface CommentService {

    Comment getCommentById(Long id);

    Boolean deleteCommentById(Long id);
}
