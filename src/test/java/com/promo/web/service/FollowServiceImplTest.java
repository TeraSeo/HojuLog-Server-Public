package com.promo.web.service;

import com.promo.web.dto.UserDto;
import com.promo.web.entity.Follow;
import com.promo.web.entity.Role;
import com.promo.web.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class FollowServiceImplTest {

    @Autowired
    private FollowService followService;

    @Autowired
    private UserService userService;

    @Test
    void createFollow() {
        UserDto user1 = UserDto.builder().email("user1@gmail.com").username("user1").password("1234").build();
        userService.createUser(user1);
        User createdUser1 = userService.getUserByEmail(user1.getEmail());

        UserDto user2 = UserDto.builder().email("user2@gmail.com").username("user2").password("1234").build();
        userService.createUser(user2);
        User createdUser2 = userService.getUserByEmail(user2.getEmail());

        followService.createFollow(createdUser1, createdUser2);

        User followingUser = userService.getUserById(createdUser1.getId());
        List<Follow> followingList = followingUser.getFollowees();
        assertEquals(1, followingList.size(), "There should be only one following for this user");

        Follow follow1 = followingList.get(0);
        assertEquals(follow1.getFollowee().getId(), createdUser1.getId(), "The follow should be associated with the correct user");

        User followedUser = userService.getUserById(createdUser2.getId());
        List<Follow> followersList = followedUser.getFollowers();
        assertEquals(1, followersList.size(), "There should be only one following for this user");

        Follow follow2 = followingList.get(0);
        assertEquals(follow2.getFollower().getId(), createdUser2.getId(), "The follow should be associated with the correct user");
    }
}