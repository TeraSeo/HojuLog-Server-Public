package com.hojunara.web.controller;

import com.hojunara.web.dto.response.*;
import com.hojunara.web.entity.*;
import com.hojunara.web.security.provider.JwtTokenProvider;
import com.hojunara.web.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for handling user-related operations.
 * <p>
 * Provides endpoints for retrieving user profiles, checking payment status,
 * viewing secret posts, and fetching ranking information.
 * All endpoints are prefixed with <code>/api/user</code>.
 * </p>
 *
 * @author Taejun Seo
 */
@RestController
@RequestMapping("api/user")
@Slf4j
public class UserController {

    private final UserService userService;
    private final PostService postService;
    private final BlogPostService blogPostService;
    private final PinnablePostService pinnablePostService;
    private final ArticlePostService articlePostService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserController(UserService userService, PostService postService, BlogPostService blogPostService, PinnablePostService pinnablePostService, ArticlePostService articlePostService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.postService = postService;
        this.blogPostService = blogPostService;
        this.pinnablePostService = pinnablePostService;
        this.articlePostService = articlePostService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Returns summarized user information for the specified user.
     *
     * @param userId the ID of the user
     * @return the summarized user information
     */
    @GetMapping("get/summarised/specific")
    public ResponseEntity<SummarizedUserDto> getSpecificSummarizedUser(@RequestHeader Long userId) {
        User user = userService.getUserById(userId);
        SummarizedUserDto summarisedUserDto = user.convertToSummarisedUserDto();
        return ResponseEntity.ok(summarisedUserDto);
    }

    /**
     * Returns detailed user information including top 5 pinned and article posts.
     *
     * @param userId the ID of the user
     * @return the detailed user profile
     */
    @GetMapping("get/specific")
    public ResponseEntity<DetailedUserDto> getSpecificDetailedUser(@RequestHeader Long userId) {
        User user = userService.getUserById(userId);

        List<PinnablePost> top5PinnablePosts = pinnablePostService.getTop5PostsByUser(userId);
        List<ArticlePost> top5Aricles = articlePostService.getTop5PostsByUser(userId);
        DetailedUserDto detailedUserDto = user.convertToDetailedUserDto(top5PinnablePosts, top5Aricles);
        return ResponseEntity.ok(detailedUserDto);
    }

    /**
     * Returns summarized user profile including only basic profile information.
     *
     * @param userId the ID of the user
     * @return the summarized user profile
     */
    @GetMapping("get/summarized/specific/profile")
    public ResponseEntity<SummarizedUserProfileDto> getSpecificSummarizedUserProfile(@RequestHeader Long userId) {
        User user = userService.getUserById(userId);
        SummarizedUserProfileDto summarizedUserProfileDto = user.convertToSummarizedUserProfileDto();
        return ResponseEntity.ok(summarizedUserProfileDto);
    }

    /**
     * Grants access for a user to view a secret blog post.
     *
     * @param viewerId the ID of the user attempting to view the post
     * @param postId the ID of the secret blog post
     * @return true if the user is allowed to view the post
     */
    @PutMapping("view/secret/post")
    public ResponseEntity<Boolean> viewSecretPost(@RequestParam Long viewerId, @RequestParam Long postId) {
        BlogPost post = blogPostService.getBlogPostById(postId);
        Boolean isValid = userService.viewSecretPost(viewerId, post);
        return ResponseEntity.ok(isValid);
    }

    /**
     * Checks whether the user has paid access to the given post.
     *
     * @param viewerId the ID of the user
     * @param postId the ID of the post
     * @return true if the user has paid for the post
     */
    @GetMapping("check/is/paid")
    public ResponseEntity<Boolean> checkIsUserPaidPost(@RequestParam Long viewerId, @RequestParam Long postId) {
        Post post = postService.getPostById(postId);
        Boolean isPaid = userService.checkIsUserPaid(viewerId, post);
        return ResponseEntity.ok(isPaid);
    }

    /**
     * Retrieves a list of the top 10 users ranked by likes received this week.
     *
     * @return list of ranked users
     */
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

    /**
     * Checks whether the user's account is currently locked.
     *
     * @param accessToken the JWT access token of the user
     * @return true if the account is locked
     */
    @GetMapping("get/is-locked")
    public ResponseEntity<Boolean> getIsAccountLocked(@RequestHeader String accessToken) {
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        String email = authentication.getName();
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user.getIsLocked());
    }
}
