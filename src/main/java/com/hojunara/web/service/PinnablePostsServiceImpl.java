package com.hojunara.web.service;

import com.hojunara.web.entity.PinnablePost;
import com.hojunara.web.entity.User;
import com.hojunara.web.exception.PostNotFoundException;
import com.hojunara.web.repository.PinnablePostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class PinnablePostsServiceImpl implements PinnablePostService {

    private final PinnablePostRepository pinnablePostRepository;
    private final UserService userService;

    @Autowired
    public PinnablePostsServiceImpl(PinnablePostRepository pinnablePostRepository, UserService userService) {
        this.pinnablePostRepository = pinnablePostRepository;
        this.userService = userService;
    }

    @Override
    public PinnablePost getPostById(Long id) {
        try {
            Optional<PinnablePost> p = pinnablePostRepository.findById(id);
            if (p.isPresent()) {
                log.info("Successfully found pinnable post with id: {}", id);
                return p.get();
            }
            throw new PostNotFoundException("Pinnable Post not found with id: " + id);
        } catch (Exception e) {
            log.error("Failed to find pinnable post with id: {}", id, e);
            throw e;
        }
    }

    @Override
    public Boolean updatePinStatus(Long postId, Long userId) {
        PinnablePost existingPost = getPostById(postId);
        User user = userService.getUserById(userId);
        try {
            if (user.getLog() >= 50) {
                Timestamp expiryDate = Timestamp.valueOf(LocalDateTime.now().plusDays(1));
                existingPost.setPinnedAdExpiry(expiryDate);

                pinnablePostRepository.save(existingPost);
                userService.updateUserLog(user, user.getLog() - 50);

                log.info("Successfully pinned post with post id: {}", postId);
                return true;
            }
            log.info("More log is needed to be pinned with user id : {}", userId);
            return false;
        } catch (Exception e) {
            log.error("Failed to pin post with post id: {}", postId, e);
            throw e;
        }
    }

    @Override
    public List<PinnablePost> getTop5PostsByUser(Long userId) {
        try {
            List<PinnablePost> articlePosts = pinnablePostRepository.findTop5ByUserIdOrderByCreatedAtDesc(userId);
            log.info("Successfully found top 5 pinnable posts by userId: {}", userId);
            return articlePosts;
        } catch (Exception e) {
            log.error("Failed to find top 5 pinnable posts by userId: {}", userId, e);
            throw e;
        }
    }

    @Override
    public Page<PinnablePost> getAllPostsByPageNUser(Long userId, Pageable pageable) {
        try {
            Page<PinnablePost> pinnablePosts = pinnablePostRepository.findAllByUserId(userId, pageable);
            log.info("Successfully found all pinnable posts by page and user");
            return pinnablePosts;
        } catch (Exception e) {
            log.error("Failed to find all pinnable posts by page", e);
            throw e;
        }
    }
}
