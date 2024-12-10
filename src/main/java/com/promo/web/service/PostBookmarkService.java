package com.promo.web.service;

import com.promo.web.entity.Post;
import com.promo.web.entity.PostBookmark;
import com.promo.web.entity.User;

import java.util.List;

public interface PostBookmarkService {
    List<PostBookmark> getWholeBookmarksByPostId(Long id);

    PostBookmark getPostBookmarkById(Long id);

    Boolean createBookmark(Post post, User user);

    Boolean deleteBookmarkById(Long postId, Long userId);

    Boolean checkIsPostBookmarkedByUser(Long userId, Long postId);
}
