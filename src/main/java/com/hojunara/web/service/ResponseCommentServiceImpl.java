package com.hojunara.web.service;

import com.hojunara.web.entity.Comment;
import com.hojunara.web.entity.ParentComment;
import com.hojunara.web.entity.ResponseComment;
import com.hojunara.web.entity.User;
import com.hojunara.web.exception.PostCommentNotFoundException;
import com.hojunara.web.repository.ResponseCommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ResponseCommentServiceImpl implements ResponseCommentService {
    private final ResponseCommentRepository responseCommentRepository;

    @Autowired
    public ResponseCommentServiceImpl(ResponseCommentRepository responseCommentRepository) {
        this.responseCommentRepository = responseCommentRepository;
    }

    @Override
    public ResponseComment getCommentById(Long id) {
        try {
            Optional<ResponseComment> comment = responseCommentRepository.findById(id);
            if (comment.isPresent()) {
                log.info("Successfully got response comment with id: {}", id);
                return comment.get();
            }
            throw new PostCommentNotFoundException("ResponseComment not found with id: " + id);
        } catch (Exception e) {
            log.error("Failed to get response comment with id: {}", id, e);
            throw e;
        }
    }

    @Override
    public ResponseComment createComment(ParentComment parentComment, User user, String content) {
        try {
            ResponseComment comment = ResponseComment.builder().parentComment(parentComment).user(user).content(content).build();

            parentComment.getResponseComments().add(comment);
            user.getComments().add(comment);
            ResponseComment createdComment = responseCommentRepository.save(comment);
            log.info("Successfully created response comment with parent comment id: {}, user id: {}", parentComment.getId(), user.getId());
            return createdComment;
        } catch (Exception e) {
            log.error("Failed to create response comment with parent comment id: {}, user id: {}", parentComment.getId(), user.getId(), e);
            return null;
        }
    }
}
