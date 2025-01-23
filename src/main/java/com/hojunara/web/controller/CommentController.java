package com.hojunara.web.controller;

import com.hojunara.web.dto.request.CommentRequestDto;
import com.hojunara.web.dto.request.ResponseCommentRequestDto;
import com.hojunara.web.dto.response.CommentResponseDto;
import com.hojunara.web.dto.response.ResponseCommentDto;
import com.hojunara.web.dto.response.SummarizedCommentDto;
import com.hojunara.web.entity.ParentComment;
import com.hojunara.web.entity.Post;
import com.hojunara.web.entity.ResponseComment;
import com.hojunara.web.entity.User;
import com.hojunara.web.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/comment")
public class CommentController {

    private final PostService postService;
    private final UserService userService;
    private final ParentCommentService parentCommentService;
    private final ResponseCommentService responseCommentService;
    private final CommentService commentService;

    @Autowired
    public CommentController(PostService postService, UserService userService, ParentCommentService parentCommentService, ResponseCommentService responseCommentService, CommentService commentService) {
        this.postService = postService;
        this.userService = userService;
        this.parentCommentService = parentCommentService;
        this.responseCommentService = responseCommentService;
        this.commentService = commentService;
    }

    @PostMapping("create")
    public ResponseEntity<Boolean> createComment(@RequestBody CommentRequestDto commentRequestDto) {
        Post post = postService.getPostById(commentRequestDto.getPostId());
        User user = userService.getUserById(commentRequestDto.getUserId());

        ParentComment createdComment = parentCommentService.createComment(post, user, commentRequestDto.getContent());
        return ResponseEntity.ok(createdComment != null);
    }

    @PostMapping("response/create")
    public ResponseEntity<Boolean> createResponseComment(@RequestBody ResponseCommentRequestDto commentRequestDto) {
        ParentComment parentComment = parentCommentService.getCommentById(commentRequestDto.getParentCommentId());
        User user = userService.getUserById(commentRequestDto.getUserId());

        ResponseComment createdComment = responseCommentService.createComment(parentComment, user, commentRequestDto.getContent());
        return ResponseEntity.ok(createdComment != null);
    }

    @GetMapping("get/specific")
    public ResponseEntity<CommentResponseDto> getPostComments(@RequestParam Long postId, @RequestHeader String userId) {
        Post post = postService.getPostById(postId);
        List<ParentComment> comments = post.getComments();
        List<SummarizedCommentDto> summarizedCommentDtoList = comments.stream().limit(20).map(comment -> comment.convertToSummarizedCommentDto(userId)).collect(Collectors.toList());
        CommentResponseDto commentResponseDto = CommentResponseDto.builder().summarizedCommentDtoList(summarizedCommentDtoList).wholeCommentsLength(Long.valueOf(comments.size())).build();
        return ResponseEntity.ok(commentResponseDto);
    }

    @GetMapping("get/response/comment")
    public ResponseEntity<ResponseCommentDto> getResponseComment(
            @RequestParam Long responseCommentId,
            @RequestHeader String userId) {

        ResponseComment responseComment = responseCommentService.getCommentById(responseCommentId);
        ResponseCommentDto responseCommentDto = responseComment.convertToResponseCommentDto(userId);

        return ResponseEntity.ok(responseCommentDto);
    }

    @DeleteMapping("delete")
    public ResponseEntity<Boolean> deleteComment(
            @RequestParam Long commentId) {
        Boolean isCommentDeleted = commentService.deleteCommentById(commentId);
        return ResponseEntity.ok(isCommentDeleted);
    }
}