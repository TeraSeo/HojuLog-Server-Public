package com.hojunara.web.controller;

import com.hojunara.web.dto.response.DetailedOwnUserDto;
import com.hojunara.web.entity.ArticlePost;
import com.hojunara.web.entity.PinnablePost;
import com.hojunara.web.entity.User;
import com.hojunara.web.service.ArticlePostService;
import com.hojunara.web.service.PinnablePostService;
import com.hojunara.web.service.PostService;
import com.hojunara.web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/own/user")
@Slf4j
public class OwnUserController {
    private final UserService userService;
    private final PostService postService;
    private final PinnablePostService pinnablePostService;
    private final ArticlePostService articlePostService;

    @Autowired
    public OwnUserController(UserService userService, PostService postService, PinnablePostService pinnablePostService, ArticlePostService articlePostService) {
        this.userService = userService;
        this.postService = postService;
        this.pinnablePostService = pinnablePostService;
        this.articlePostService = articlePostService;
    }

    @GetMapping("get/specific")
    public ResponseEntity<DetailedOwnUserDto> getSpecificOwnUser(@RequestHeader Long userId) {
        User user = userService.getUserById(userId);

        List<PinnablePost> top5PinnablePosts = pinnablePostService.getTop5PostsByUser(userId);
        List<ArticlePost> top5Aricles = articlePostService.getTop5PostsByUser(userId);
        DetailedOwnUserDto detailedOwnUserDto = user.convertToDetailedOwnUserDto(top5PinnablePosts, top5Aricles);
        return ResponseEntity.ok(detailedOwnUserDto);
    }

    @PutMapping("update")
    public ResponseEntity<Boolean> updateUser(@RequestHeader("userId") Long userId,
                                              @RequestParam("username") String username,
                                              @RequestParam("description") String description,
                                              @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture) {
        Boolean isUpdated = userService.updateUser(userId, username, description, profilePicture);
        return ResponseEntity.ok(isUpdated);
    }

    @PutMapping("update/attendance")
    public ResponseEntity<Boolean> updateAttendance(@RequestHeader("userId") Long userId) {
        Boolean isAttended = userService.updateAttendance(userId);
        return ResponseEntity.ok(isAttended);
    }

    @PutMapping("update/account/lock")
    public ResponseEntity<Boolean> lockAccount(@RequestHeader("userId") Long userId) {
        Boolean isLocked = userService.lockAccount(userId);
        return ResponseEntity.ok(isLocked);
    }
}
