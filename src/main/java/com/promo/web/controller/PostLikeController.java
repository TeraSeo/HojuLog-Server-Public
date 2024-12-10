package com.promo.web.controller;

import com.promo.web.dto.request.PostLikeRequestDto;
import com.promo.web.entity.Post;
import com.promo.web.entity.User;
import com.promo.web.service.PostLikeService;
import com.promo.web.service.PostService;
import com.promo.web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/post-like")
@Slf4j
public class PostLikeController {

    private final UserService userService;
    private final PostService postService;
    private final PostLikeService postLikeService;

    @Autowired
    public PostLikeController(UserService userService, PostService postService, PostLikeService postLikeService) {
        this.userService = userService;
        this.postService = postService;
        this.postLikeService = postLikeService;
    }


    @PostMapping("create")
    public ResponseEntity<Long> createPostLike(@RequestBody PostLikeRequestDto postLikeRequestDto) {
        User user = userService.getUserById(postLikeRequestDto.getUserId());
        Post post = postService.getPostById(postLikeRequestDto.getPostId());
        Long likesCount = postLikeService.createLike(post, user);

        return ResponseEntity.ok(likesCount);
    }

    @DeleteMapping("delete")
    public ResponseEntity<Long> deletePostLike(@RequestParam Long postId, @RequestParam Long userId) {
        Long likesCount = postLikeService.deletePostLikeById(userId, postId);
        return ResponseEntity.ok(likesCount);
    }

}
