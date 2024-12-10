package com.hojunara.web.controller;

import com.hojunara.web.dto.request.CommentLikeRequestDto;
import com.hojunara.web.entity.Comment;
import com.hojunara.web.entity.User;
import com.hojunara.web.service.CommentLikeService;
import com.hojunara.web.service.CommentService;
import com.hojunara.web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/comment-like")
@Slf4j
public class CommentLikeController {

    private final UserService userService;
    private final CommentService commentService;
    private final CommentLikeService commentLikeService;

    @Autowired
    public CommentLikeController(UserService userService, CommentService commentService, CommentLikeService commentLikeService) {
        this.userService = userService;
        this.commentService = commentService;
        this.commentLikeService = commentLikeService;
    }

    @PostMapping("create")
    public ResponseEntity<Long> createPostLike(@RequestBody CommentLikeRequestDto commentLikeRequestDto) {
        User user = userService.getUserById(commentLikeRequestDto.getUserId());
        Comment comment = commentService.getCommentById(commentLikeRequestDto.getCommentId());
        Long likesCount = commentLikeService.createLike(comment, user);

        return ResponseEntity.ok(likesCount);
    }

    @DeleteMapping("delete")
    public ResponseEntity<Long> deletePostLike(@RequestParam Long commentId, @RequestParam Long userId) {
        Long likesCount = commentLikeService.deleteCommentLikeById(commentId, userId);
        return ResponseEntity.ok(likesCount);
    }

}
