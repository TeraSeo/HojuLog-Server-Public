package com.promo.web.controller;

import com.promo.web.dto.request.CommentRequestDto;
import com.promo.web.dto.response.CommentResponseDto;
import com.promo.web.dto.response.SummarizedCommentDto;
import com.promo.web.entity.Comment;
import com.promo.web.entity.Post;
import com.promo.web.entity.User;
import com.promo.web.service.CommentService;
import com.promo.web.service.PostService;
import com.promo.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/comment")
public class CommentController {

    private final PostService postService;
    private final UserService userService;
    private final CommentService commentService;

    @Autowired
    public CommentController(PostService postService, UserService userService, CommentService commentService) {
        this.postService = postService;
        this.userService = userService;
        this.commentService = commentService;
    }

    @PostMapping("create")
    public ResponseEntity<Boolean> createComment(@RequestBody CommentRequestDto commentRequestDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Post post = postService.getPostById(commentRequestDto.getPostId());
        User user = userService.getUserByEmail(email);

        Comment createdComment = commentService.createComment(post, user, commentRequestDto.getContent());
        return ResponseEntity.ok(createdComment != null);
    }

    @GetMapping("get/specific")
    public ResponseEntity<CommentResponseDto> getPostComments(@RequestParam Long postId, @RequestParam Long userId) {
        Post post = postService.getPostById(postId);
        List<Comment> comments = post.getComments();
        List<SummarizedCommentDto> summarizedCommentDtoList = comments.stream().limit(20).map(comment -> comment.convertToSummarizedCommentDto(userId)).collect(Collectors.toList());
        CommentResponseDto commentResponseDto = CommentResponseDto.builder().summarizedCommentDtoList(summarizedCommentDtoList).wholeCommentsLength(Long.valueOf(comments.size())).build();
        return ResponseEntity.ok(commentResponseDto);
    }
}