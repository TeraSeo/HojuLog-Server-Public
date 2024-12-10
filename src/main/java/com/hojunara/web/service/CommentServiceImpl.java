package com.hojunara.web.service;

import com.hojunara.web.entity.Comment;
import com.hojunara.web.entity.Post;
import com.hojunara.web.entity.User;
import com.hojunara.web.exception.PostCommentNotFoundException;
import com.hojunara.web.repository.CommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public List<Comment> getWholeCommentsByPostId(Long id) {
        try {
            List<Comment> postComments = commentRepository.findByPostId(id);
            log.info("Successfully got whole post comments with post id: {}", id);
            return postComments;
        } catch (Exception e) {
            log.error("Failed to get whole post comments with post id: {}", id, e);
            throw e;
        }
    }

    @Override
    public Comment getCommentById(Long id) {
        try {
            Optional<Comment> comment = commentRepository.findById(id);
            if (comment.isPresent()) {
                log.info("Successfully got post comment with id: {}", id);
                return comment.get();
            }
            throw new PostCommentNotFoundException("PostLike not found with id: " + id);
        } catch (Exception e) {
            log.error("Failed to get post comment with id: {}", id, e);
            throw e;
        }
    }

    @Override
    public Comment createComment(Post post, User user, String content) {
        try {
            Comment comment = Comment.builder().post(post).user(user).content(content).build();

            post.getComments().add(comment);
            user.getComments().add(comment);
            Comment createdComment = commentRepository.save(comment);
            log.info("Successfully created post comment with post id: {}, user id: {}", post.getId(), user.getId());
            return createdComment;
        } catch (Exception e) {
            log.error("Failed to create post comment with post id: {}, user id: {}", post.getId(), user.getId(), e);
            return null;
        }
    }

    @Override
    public void deleteCommentById(Long id) {
        try {
            commentRepository.deleteById(id);
            log.info("Successfully deleted post comment with id: {}", id);
        } catch (Exception e) {
            log.error("Failed to delete post comment with id: {}", id, e);
            throw e;
        }
    }
}
