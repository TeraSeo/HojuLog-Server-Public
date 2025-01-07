package com.hojunara.web.service;

import com.hojunara.web.entity.ParentComment;
import com.hojunara.web.entity.Post;
import com.hojunara.web.entity.User;
import com.hojunara.web.exception.PostCommentNotFoundException;
import com.hojunara.web.repository.ParentCommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ParentCommentServiceImpl implements ParentCommentService {

    private final ParentCommentRepository parentCommentRepository;

    @Autowired
    public ParentCommentServiceImpl(ParentCommentRepository parentCommentRepository) {
        this.parentCommentRepository = parentCommentRepository;
    }

    @Override
    public ParentComment getCommentById(Long id) {
        try {
            Optional<ParentComment> comment = parentCommentRepository.findById(id);
            if (comment.isPresent()) {
                log.info("Successfully got parent comment with id: {}", id);
                return comment.get();
            }
            throw new PostCommentNotFoundException("ParentComment not found with id: " + id);
        } catch (Exception e) {
            log.error("Failed to get parent comment with id: {}", id, e);
            throw e;
        }
    }

    @Override
    public ParentComment createComment(Post post, User user, String content) {
        if (post == null || user == null) {
            throw new IllegalArgumentException("Post and User must be provided and already persisted.");
        }

        try {
            ParentComment comment = ParentComment.builder().post(post).user(user).content(content).build();

            post.getComments().add(comment);
            user.getComments().add(comment);
            ParentComment createdComment = parentCommentRepository.save(comment);
            log.info("Successfully created parent comment with post id: {}, user id: {}", post.getId(), user.getId());
            return createdComment;
        } catch (Exception e) {
            log.error("Failed to create parent comment with post id: {}, user id: {}", post.getId(), user.getId(), e);
            return null;
        }
    }
}
