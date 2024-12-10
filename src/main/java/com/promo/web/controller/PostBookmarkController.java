package com.promo.web.controller;

import com.promo.web.dto.request.PostBookmarkRequestDto;
import com.promo.web.entity.Post;
import com.promo.web.entity.User;
import com.promo.web.service.PostBookmarkService;
import com.promo.web.service.PostService;
import com.promo.web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/post-bookmark")
@Slf4j
public class PostBookmarkController {

    private final UserService userService;
    private final PostService postService;
    private final PostBookmarkService postBookmarkService;

    @Autowired
    public PostBookmarkController(UserService userService, PostService postService, PostBookmarkService postBookmarkService) {
        this.userService = userService;
        this.postService = postService;
        this.postBookmarkService = postBookmarkService;
    }

    @PostMapping("create")
    public ResponseEntity<Boolean> createPostBookmark(@RequestBody PostBookmarkRequestDto postBookmarkRequestDto) {
        User user = userService.getUserById(postBookmarkRequestDto.getUserId());
        Post post = postService.getPostById(postBookmarkRequestDto.getPostId());
        Boolean isCreated = postBookmarkService.createBookmark(post, user);
        return ResponseEntity.ok(isCreated);
    }

    @DeleteMapping("delete")
    public ResponseEntity<Boolean> deletePostBookmark(@RequestParam Long postId, @RequestParam Long userId) {
        Boolean isDeleted = postBookmarkService.deleteBookmarkById(userId, postId);
        return ResponseEntity.ok(isDeleted);
    }
}
