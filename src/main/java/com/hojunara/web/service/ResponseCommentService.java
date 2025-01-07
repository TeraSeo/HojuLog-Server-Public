package com.hojunara.web.service;

import com.hojunara.web.entity.ParentComment;
import com.hojunara.web.entity.ResponseComment;
import com.hojunara.web.entity.User;

public interface ResponseCommentService {
    ResponseComment getCommentById(Long id);

    ResponseComment createComment(ParentComment parentComment, User user, String content);
}
