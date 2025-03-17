package com.hojunara.web.service;

import com.hojunara.web.entity.PinnablePost;

public interface PinnablePostService {
    PinnablePost getPostById(Long id);

    Boolean updatePinStatus(Long postId, Long userId);
}
