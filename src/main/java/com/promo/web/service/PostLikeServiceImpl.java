package com.promo.web.service;

import com.promo.web.entity.Post;
import com.promo.web.entity.PostLike;
import com.promo.web.entity.User;
import com.promo.web.exception.PostLikeNotFoundByUserNPostException;
import com.promo.web.exception.PostLikeNotFoundException;
import com.promo.web.repository.PostLikeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class PostLikeServiceImpl implements PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostService postService;

    @Autowired
    public PostLikeServiceImpl(PostLikeRepository postLikeRepository, PostService postService) {
        this.postLikeRepository = postLikeRepository;
        this.postService = postService;
    }

    @Override
    public List<PostLike> getWholeLikesByPostId(Long id) {
        Post post = postService.getPostById(id);

        try {
            List<PostLike> postLikes = post.getLikes();
            log.info("Successfully got whole post likes with post id: {}", id);
            return postLikes;
        } catch (Exception e) {
            log.error("Failed to get whole post likes with post id: {}", id, e);
            throw e;
        }
    }

    @Override
    public PostLike getPostLikeById(Long id) {
        try {
            Optional<PostLike> postLike = postLikeRepository.findById(id);
            if (postLike.isPresent()) {
                log.info("Successfully got post like with id: {}", id);
                return postLike.get();
            }
            throw new PostLikeNotFoundException("PostLike not found with id: " + id);
        } catch (Exception e) {
            log.error("Failed to get post like with id: {}", id, e);
            throw e;
        }
    }

    @Override
    public Long createLike(Post post, User user) {
        try {
            if (checkIsPostLikedByUser(user.getId(), post.getId())) {
                return Long.valueOf(post.getLikes().size());
            }

            PostLike postLike = PostLike.builder().post(post).user(user).createdAt(new Timestamp(System.currentTimeMillis())).build();
            post.getLikes().add(postLike);
            user.getPostLikes().add(postLike);
            postLikeRepository.save(postLike);

            log.info("Successfully created post like with post id: {}, user id: {}", post.getId(), user.getId());
            return Long.valueOf(post.getLikes().size());
        } catch (Exception e) {
            log.error("Failed to create post like with post id: {}, user id: {}", post.getId(), user.getId(), e);
            throw e;
        }
    }

    @Override
    public Long deletePostLikeById(Long userId, Long postId) {
        try {
            Optional<PostLike> p = postLikeRepository.findByUserIdAndPostId(userId, postId);
            if (p.isPresent()) {
                PostLike postLike = p.get();

                Post post = postLike.getPost();
                User user = postLike.getUser();

                if (post != null) post.getLikes().remove(postLike);
                if (user != null) user.getPostLikes().remove(postLike);

                postLikeRepository.deleteById(postLike.getId());
                log.info("Successfully deleted post like with id: {}", postLike.getId());

                return Long.valueOf(post.getLikes().size());
            }

            throw new PostLikeNotFoundByUserNPostException("PostLike not found by user id: " + userId + " and post id: " + postId);
        } catch (Exception e) {
            log.error("Failed to delete post like with post id: {}  user id: {}", postId, userId, e);
            throw e;
        }
    }

    @Override
    public Boolean checkIsPostLikedByUser(Long userId, Long postId) {
        return postLikeRepository.existsByUserIdAndPostId(userId, postId);
    }
}
