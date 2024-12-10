package com.hojunara.web.service;

import com.hojunara.web.entity.Post;
import com.hojunara.web.entity.PostBookmark;
import com.hojunara.web.entity.User;

import java.util.List;

public interface PostBookmarkService {
    List<PostBookmark> getWholeBookmarksByPostId(Long id);

    PostBookmark getPostBookmarkById(Long id);

    Boolean createBookmark(Post post, User user);

    Boolean deleteBookmarkById(Long postId, Long userId);

    Boolean checkIsPostBookmarkedByUser(Long userId, Long postId);
}
