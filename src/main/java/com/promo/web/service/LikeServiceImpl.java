package com.promo.web.service;

import com.promo.web.entity.Post;
import com.promo.web.entity.PostLike;
import com.promo.web.entity.User;
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
public class LikeServiceImpl implements LikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostService postService;

    @Autowired
    public LikeServiceImpl(PostLikeRepository postLikeRepository, PostService postService) {
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
    public void createLike(Post post, User user) {
        try {
            PostLike postLike = PostLike.builder().post(post).user(user).createdAt(new Timestamp(System.currentTimeMillis())).build();

            post.getLikes().add(postLike);
            user.getLikes().add(postLike);
            postLikeRepository.save(postLike);
            log.info("Successfully created post like with post id: {}, user id: {}", post.getId(), user.getId());
        } catch (Exception e) {
            log.error("Failed to create post like with post id: {}, user id: {}", post.getId(), user.getId(), e);
            throw e;
        }
    }

    @Override
    public void deletePostLikeById(Long id) {
        try {
            postLikeRepository.deleteById(id);
            log.info("Successfully deleted post like with id: {}", id);
        } catch (Exception e) {
            log.error("Failed to delete post like with id: {}", id, e);
            throw e;
        }
    }
}
