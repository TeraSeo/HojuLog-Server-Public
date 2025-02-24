package com.hojunara.web.controller;

import com.hojunara.web.dto.response.DetailedUserDto;
import com.hojunara.web.dto.response.SummarizedUserDto;
import com.hojunara.web.dto.response.SummarizedUserProfileDto;
import com.hojunara.web.entity.Post;
import com.hojunara.web.entity.User;
import com.hojunara.web.service.PostService;
import com.hojunara.web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user")
@Slf4j
public class UserController {

    private final UserService userService;
    private final PostService postService;

    @Autowired
    public UserController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @GetMapping("get/summarised/specific")
    public ResponseEntity<SummarizedUserDto> getSpecificSummarizedUser(@RequestHeader Long userId) {
        User user = userService.getUserById(userId);
        SummarizedUserDto summarisedUserDto = user.convertToSummarisedUserDto();
        return ResponseEntity.ok(summarisedUserDto);
    }

    @GetMapping("get/specific")
    public ResponseEntity<DetailedUserDto> getSpecificDetailedUser(@RequestHeader Long userId) {
        User user = userService.getUserById(userId);
        Long likeCountThisWeek = postService.calculateLikeCountThisWeek(userId);
        DetailedUserDto detailedUserDto = user.convertToDetailedUserDto();
        detailedUserDto.setLikeCountThisWeek(likeCountThisWeek);
        return ResponseEntity.ok(detailedUserDto);
    }

    @GetMapping("get/summarized/specific/profile")
    public ResponseEntity<SummarizedUserProfileDto> getSpecificSummarizedUserProfile(@RequestHeader Long userId) {
        User user = userService.getUserById(userId);
        SummarizedUserProfileDto summarizedUserProfileDto = user.convertToSummarizedUserProfileDto();
        return ResponseEntity.ok(summarizedUserProfileDto);
    }

    @PutMapping("view/secret/post")
    public ResponseEntity<Boolean> viewSecretPost(@RequestParam Long viewerId, @RequestParam Long postId) {
        Post post = postService.getPostById(postId);
        Boolean isValid = userService.viewSecretPost(viewerId, post);
        return ResponseEntity.ok(isValid);
    }

    @GetMapping("check/is/paid")
    public ResponseEntity<Boolean> checkIsUserPaidPost(@RequestParam Long viewerId, @RequestParam Long postId) {
        Post post = postService.getPostById(postId);
        Boolean isPaid = userService.checkIsUserPaid(viewerId, post);
        return ResponseEntity.ok(isPaid);
    }

}
