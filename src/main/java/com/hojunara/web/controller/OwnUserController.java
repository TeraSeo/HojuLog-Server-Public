package com.hojunara.web.controller;

import com.hojunara.web.dto.response.DetailedOwnUserDto;
import com.hojunara.web.entity.User;
import com.hojunara.web.service.PostService;
import com.hojunara.web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/own/user")
@Slf4j
public class OwnUserController {
    private final UserService userService;
    private final PostService postService;

    @Autowired
    public OwnUserController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @GetMapping("get/specific")
    public ResponseEntity<DetailedOwnUserDto> getSpecificOwnUser(@RequestHeader Long userId) {
        User user = userService.getUserById(userId);
        Long likeCountThisWeek = postService.calculateLikeCountThisWeek(userId);
        DetailedOwnUserDto detailedOwnUserDto = user.convertToDetailedOwnUserDto();
        detailedOwnUserDto.setLikeCountThisWeek(likeCountThisWeek);
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
}
