package com.hojunara.web.service;

import com.hojunara.web.entity.*;
import com.hojunara.web.exception.PostNotFoundException;
import com.hojunara.web.exception.PostPaginationFailedException;
import com.hojunara.web.repository.PostRepository;
import com.hojunara.web.repository.ViewedUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ViewedUserRepository viewedUserRepository;
    private final UserService userService;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, ViewedUserRepository viewedUserRepository, UserService userService) {
        this.postRepository = postRepository;
        this.viewedUserRepository = viewedUserRepository;
        this.userService = userService;
    }

    @Override
    public List<Post> getWholePosts() {
        try {
            List<Post> posts = postRepository.findAll();
            log.info("Successfully got Whole Posts");
            return posts;
        } catch (Exception e) {
            log.error("Failed to get Whole Posts", e);
            throw e;
        }
    }

    @Override
    public Page<Post> getPostsByPageNCondition(Pageable pageable, String condition) {
        try {
            switch (condition) {
                case "Latest":
                    log.info("Successfully found latest post with pagination");
                    return postRepository.findAllByOrderByUpdatedAtDesc(pageable);
                case "Oldest":
                    log.info("Successfully found oldest post with pagination");
                    return postRepository.findAllByOrderByUpdatedAtAsc(pageable);
//                case "Popular":
//                    log.info("Successfully found popular post with pagination");
//                    return postRepository.findAllByOrderByLikesCountDesc(pageable);
//                case "Unpopular":
//                    log.info("Successfully found unpopular post with pagination");
//                    return postRepository.findAllByOrderByLikesCountAsc(pageable);
                default:
                    throw new PostPaginationFailedException("Post not found with condition: " + condition);
            }
        } catch (Exception e) {
            log.error("Failed to find post with paging and condition: {}", condition, e);
            throw e;
        }
    }

    @Override
    public Page<Post> getPostsByPageNUser(Long userId, Pageable pageable) {
        User user = userService.getUserById(userId);
        List<Post> posts = user.getPosts();
        try {
            log.info("Successfully found own posts with paging and user id : {}", userId);
            return convertPostsAsPage(posts, pageable);
        } catch (Exception e) {
            log.error("Failed to find own posts with paging and user id: {}", userId, e);
            throw e;
        }
    }

    @Override
    public Page<Post> getPostsByPageNLiked(Long userId, Pageable pageable) {
        User user = userService.getUserById(userId);
        List<Post> userLikedPosts = user.getPostLikes().stream().map(PostLike::getPost).collect(Collectors.toList());
        try {
            log.info("Successfully found liked posts with paging and user id : {}", userId);
            return convertPostsAsPage(userLikedPosts, pageable);
        } catch (Exception e) {
            log.error("Failed to find liked posts with paging and user id: {}", userId, e);
            throw e;
        }
    }

    @Override
    public Post getPostById(Long id) {
        try {
            Optional<Post> p = postRepository.findById(id);
            if (p.isPresent()) {
                log.info("Successfully found post with id: {}", id);
                return p.get();
            }
            throw new PostNotFoundException("Post not found with id: " + id);
        } catch (Exception e) {
            log.error("Failed to find post with id: {}", id, e);
            throw e;
        }
    }

    @Override
    public void addViewCount(Long postId) {
        try {
            postRepository.incrementViewCount(postId);
            log.info("Successfully added view count");
        } catch (Exception e) {
            log.error("Failed to add view count", e);
            throw e;
        }
    }

    @Override
    public Page<Post> convertPostsAsPage(List<Post> posts, Pageable pageable) {
        try {
            int start = (int) pageable.getOffset();
            int end = Math.min(start + pageable.getPageSize(), posts.size());

            List<Post> paginatedPosts = posts.subList(start, end);

            return new PageImpl<>(paginatedPosts, pageable, posts.size());
        } catch (Exception e) {
            log.error("Failed to convert Post as Page");
            throw e;
        }
    }

    @Override
    public Boolean removePost(Long postId) {
        Post existingPost = getPostById(postId);
        try {
            userService.removePaidPostContaining(existingPost);
            postRepository.delete(existingPost);
            return true;
        } catch (Exception e) {
            log.error("Failed to remove post with id: ", postId, e);
            return false;
        }
    }

//    @Override
//    public Long calculateLikeCountThisWeek(Long userId) {
//        try {
//            return postRepository.countLikesThisWeekByUserId(userId);
//        } catch (Exception e) {
//            log.error("Failed to calculate like count this week");
//            throw e;
//        }
//    }
}
