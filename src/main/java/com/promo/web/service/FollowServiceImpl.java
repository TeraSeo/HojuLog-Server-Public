package com.promo.web.service;

import com.promo.web.entity.Follow;
import com.promo.web.entity.User;
import com.promo.web.exception.FollowNotFoundException;
import com.promo.web.repository.FollowRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Optional;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;

    @Autowired
    public FollowServiceImpl(FollowRepository followRepository) {
        this.followRepository = followRepository;
    }

    @Override
    public Follow getFollowById(Long id) {
        try {
            Optional<Follow> follow = followRepository.findById(id);
            if (follow.isPresent()) {
                log.info("Successfully got follow by id: {}", id);
                return follow.get();
            }
            throw new FollowNotFoundException("Follow not found with id: " + id);
        } catch (Exception e) {
            log.error("Failed to get follow by id: {}", id, e);
            throw e;
        }
    }

    @Override
    public void createFollow(User followingUser, User followedUser) {
        if (followRepository.existsByFollowerAndFollowee(followingUser, followedUser)) {
            log.warn("Follow relationship already exists between {} and {}", followingUser.getId(), followedUser.getId());
            return;
        }

        try {
            Follow follow = Follow.builder().followee(followingUser).follower(followedUser).createdAt(new Timestamp(System.currentTimeMillis())).build();

            followingUser.getFollowees().add(follow);
            followedUser.getFollowers().add(follow);

            followRepository.save(follow);
            log.info("Successfully created follow with followingId: {}, followedId: {}", followingUser.getId(), followedUser.getId());
        } catch (Exception e) {
            log.error("Failed to create follow with followingId: {}, followedId: {}", followingUser.getId(), followedUser.getId(), e);
            throw e;
        }
    }

    @Override
    public void deleteFollowById(Long id) {
        try {
            followRepository.deleteById(id);
            log.info("Successfully deleted follow with id: {}", id);
        } catch (Exception e) {
            log.error("Failed to delete follow with id: {}", id, e);
            throw e;
        }
    }
}
