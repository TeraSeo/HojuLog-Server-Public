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

/**
 * REST controller for managing comments and response comments on posts.
 * <p>
 * Provides endpoints for creating, retrieving, and deleting parent and child comments.
 * All endpoints are prefixed with <code>/api/comment</code>.
 * </p>
 *
 * @author Taejun Seo
 */
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

    /**
     * Creates a new parent comment on a post.
     *
     * @param commentRequestDto the comment creation request containing post ID, user ID, and content
     * @return {@code true} if the comment was created successfully
     */
    @PostMapping("create")
    public ResponseEntity<Boolean> createComment(@RequestBody CommentRequestDto commentRequestDto) {
        Post post = postService.getPostById(commentRequestDto.getPostId());
        User user = userService.getUserById(commentRequestDto.getUserId());

        ParentComment createdComment = parentCommentService.createComment(post, user, commentRequestDto.getContent());
        return ResponseEntity.ok(createdComment != null);
    }

    /**
     * Creates a response (child) comment to a parent comment.
     *
     * @param commentRequestDto the request containing parent comment ID, user ID, and content
     * @return {@code true} if the response comment was created successfully
     */
    @PostMapping("response/create")
    public ResponseEntity<Boolean> createResponseComment(@RequestBody ResponseCommentRequestDto commentRequestDto) {
        ParentComment parentComment = parentCommentService.getCommentById(commentRequestDto.getParentCommentId());
        User user = userService.getUserById(commentRequestDto.getUserId());

        ResponseComment createdComment = responseCommentService.createComment(parentComment, user, commentRequestDto.getContent());
        return ResponseEntity.ok(createdComment != null);
    }

    /**
     * Retrieves up to 20 summarized parent comments for a specific post.
     *
     * @param postId the ID of the post
     * @param userId the ID of the requesting user (used for like/dislike context)
     * @return a {@link CommentResponseDto} containing the comment list and total count
     */
    @GetMapping("get/specific")
    public ResponseEntity<CommentResponseDto> getPostComments(@RequestParam Long postId, @RequestHeader String userId) {
        Post post = postService.getPostById(postId);
        List<ParentComment> comments = post.getComments();
        List<SummarizedCommentDto> summarizedCommentDtoList = comments.stream().limit(20).map(comment -> comment.convertToSummarizedCommentDto(userId)).collect(Collectors.toList());
        CommentResponseDto commentResponseDto = CommentResponseDto.builder().summarizedCommentDtoList(summarizedCommentDtoList).wholeCommentsLength(Long.valueOf(comments.size())).build();
        return ResponseEntity.ok(commentResponseDto);
    }

    /**
     * Retrieves a specific response (child) comment by its ID.
     *
     * @param responseCommentId the ID of the response comment
     * @param userId the ID of the requesting user
     * @return a {@link ResponseCommentDto} with the comment details
     */
    @GetMapping("get/response/comment")
    public ResponseEntity<ResponseCommentDto> getResponseComment(
            @RequestParam Long responseCommentId,
            @RequestHeader String userId) {

        ResponseComment responseComment = responseCommentService.getCommentById(responseCommentId);
        ResponseCommentDto responseCommentDto = responseComment.convertToResponseCommentDto(userId);

        return ResponseEntity.ok(responseCommentDto);
    }

    /**
     * Deletes a comment (parent or response) by its ID.
     *
     * @param commentId the ID of the comment to delete
     * @return {@code true} if the comment was deleted successfully
     */
    @DeleteMapping("delete")
    public ResponseEntity<Boolean> deleteComment(
            @RequestParam Long commentId) {
        Boolean isCommentDeleted = commentService.deleteCommentById(commentId);
        return ResponseEntity.ok(isCommentDeleted);
    }
}