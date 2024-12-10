package com.hojunara.web.service;

import com.hojunara.web.entity.*;
import com.hojunara.web.exception.CommentLikeNotFoundByCommentNUserException;
import com.hojunara.web.exception.CommentLikeNotFoundException;
import com.hojunara.web.repository.CommentLikeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class CommentLikeServiceImpl implements CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentService commentService;

    @Autowired
    public CommentLikeServiceImpl(CommentLikeRepository commentLikeRepository, CommentService commentService) {
        this.commentLikeRepository = commentLikeRepository;
        this.commentService = commentService;
    }

    @Override
    public List<CommentLike> getWholeLikesByCommentId(Long id) {
        Comment comment = commentService.getCommentById(id);

        try {
            List<CommentLike> commentLikes = comment.getLikes();
            log.info("Successfully got whole comment likes with comment id: {}", id);
            return commentLikes;
        } catch (Exception e) {
            log.error("Failed to get whole comment likes with comment id: {}", id, e);
            throw e;
        }
    }

    @Override
    public CommentLike getCommentLikeById(Long id) {
        try {
            Optional<CommentLike> commentLike = commentLikeRepository.findById(id);
            if (commentLike.isPresent()) {
                log.info("Successfully got comment like with id: {}", id);
                return commentLike.get();
            }
            throw new CommentLikeNotFoundException("CommentLike not found with id: " + id);
        } catch (Exception e) {
            log.error("Failed to get comment like with id: {}", id, e);
            throw e;
        }
    }

    @Override
    public Long createLike(Comment comment, User user) {
        try {
            CommentLike commentLike = CommentLike.builder().comment(comment).user(user).createdAt(new Timestamp(System.currentTimeMillis())).build();

            comment.getLikes().add(commentLike);
            user.getCommentLikes().add(commentLike);
            commentLikeRepository.save(commentLike);

            log.info("Successfully created comment like with comment id: {}, user id: {}", comment.getId(), user.getId());
            return Long.valueOf(comment.getLikes().size());
        } catch (Exception e) {
            log.error("Failed to create comment like with comment id: {}, user id: {}", comment.getId(), user.getId(), e);
            throw e;
        }
    }

    @Override
    public Long deleteCommentLikeById(Long commentId, Long userId) {
        try {
            Optional<CommentLike> c = commentLikeRepository.findByUserIdAndCommentId(userId, commentId);
            if (c.isPresent()) {
                CommentLike commentLike = c.get();

                Comment comment = commentLike.getComment();
                User user = commentLike.getUser();

                if (comment != null) comment.getLikes().remove(commentLike);
                if (user != null) user.getCommentLikes().remove(commentLike);

                commentLikeRepository.deleteById(commentLike.getId());
                log.info("Successfully deleted comment like with id: {}", commentLike.getId());

                return Long.valueOf(comment.getLikes().size());
            }

            throw new CommentLikeNotFoundByCommentNUserException("CommentLike Not Found with user id: " + userId + ", comment id: " + commentId);
        } catch (Exception e) {
            log.error("Failed to delete comment like", e);
            throw e;
        }
    }
}
