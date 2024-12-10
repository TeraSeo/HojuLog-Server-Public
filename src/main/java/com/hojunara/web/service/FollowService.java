package com.hojunara.web.service;

import com.hojunara.web.entity.Follow;
import com.hojunara.web.entity.User;

public interface FollowService {

    Follow getFollowById(Long id);

    void createFollow(User followingUser, User followedUser);

    void deleteFollowById(Long id);
}
