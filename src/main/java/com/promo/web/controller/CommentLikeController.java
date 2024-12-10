package com.promo.web.controller;

import com.promo.web.dto.request.CommentLikeRequestDto;
import com.promo.web.entity.Comment;
import com.promo.web.entity.User;
import com.promo.web.service.CommentLikeService;
import com.promo.web.service.CommentService;
import com.promo.web.service.UserService;
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
