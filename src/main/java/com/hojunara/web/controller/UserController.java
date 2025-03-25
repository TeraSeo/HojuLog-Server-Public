package com.hojunara.web.controller;

import com.hojunara.web.dto.response.*;
import com.hojunara.web.entity.*;
import com.hojunara.web.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/user")
@Slf4j
public class UserController {

    private final UserService userService;
    private final PostService postService;
    private final BlogPostService blogPostService;
    private final PinnablePostService pinnablePostService;
    private final ArticlePostService articlePostService;

    @Autowired
    public UserController(UserService userService, PostService postService, BlogPostService blogPostService, PinnablePostService pinnablePostService, ArticlePostService articlePostService) {
        this.userService = userService;
        this.postService = postService;
        this.blogPostService = blogPostService;
        this.pinnablePostService = pinnablePostService;
        this.articlePostService = articlePostService;
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

        List<PinnablePost> top5PinnablePosts = pinnablePostService.getTop5PostsByUser(userId);
        List<ArticlePost> top5Aricles = articlePostService.getTop5PostsByUser(userId);
        DetailedUserDto detailedUserDto = user.convertToDetailedUserDto(top5PinnablePosts, top5Aricles);
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
        BlogPost post = blogPostService.getBlogPostById(postId);
        Boolean isValid = userService.viewSecretPost(viewerId, post);
        return ResponseEntity.ok(isValid);
    }

    @GetMapping("check/is/paid")
    public ResponseEntity<Boolean> checkIsUserPaidPost(@RequestParam Long viewerId, @RequestParam Long postId) {
        Post post = postService.getPostById(postId);
        Boolean isPaid = userService.checkIsUserPaid(viewerId, post);
        return ResponseEntity.ok(isPaid);
    }

    @GetMapping("get/top10/users")
    public ResponseEntity<List<UserRankDto>> getTop10WeeklyLikedUsers() {
        List<User> users = userService.getTop10UsersByLikesThisWeek();
        List<UserRankDto> userRankDtoList = users
                .stream()
                .map(user -> {
                    UserRankDto userRankDto = user.convertToUserRankDto();
                    return userRankDto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(userRankDtoList);
    }
}
