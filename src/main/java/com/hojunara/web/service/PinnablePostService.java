package com.hojunara.web.service;

import com.hojunara.web.entity.PinnablePost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PinnablePostService {
    PinnablePost getPostById(Long id);

    Boolean updatePinStatus(Long postId, Long userId);

    Page<PinnablePost> getAllPostsByPageNUser(Long userId, Pageable pageable);

    List<PinnablePost> getTop5PostsByUser(Long userId);
}
