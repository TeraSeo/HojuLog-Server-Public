package com.promo.web.service;

import com.promo.web.entity.Follow;
import com.promo.web.entity.User;

public interface FollowService {

    Follow getFollowById(Long id);

    void createFollow(User followingUser, User followedUser);

    void deleteFollowById(Long id);
}
