package com.hojunara.web.service;

import com.hojunara.web.entity.ParentComment;
import com.hojunara.web.entity.Post;
import com.hojunara.web.entity.User;

public interface ParentCommentService {
    ParentComment getCommentById(Long id);

    ParentComment createComment(Post post, User user, String content);
}
