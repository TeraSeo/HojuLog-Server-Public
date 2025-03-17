package com.hojunara.web.service;

import com.hojunara.web.aws.s3.AwsFileService;
import com.hojunara.web.dto.request.AdminUpdateUserDto;
import com.hojunara.web.dto.request.UserDto;
import com.hojunara.web.entity.BlogPost;
import com.hojunara.web.entity.Post;
import com.hojunara.web.entity.RegistrationMethod;
import com.hojunara.web.entity.User;
import com.hojunara.web.exception.UserAlreadyExistsException;
import com.hojunara.web.exception.UserNotFoundException;
import com.hojunara.web.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AwsFileService awsFileService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final NotificationService notificationService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, AwsFileService awsFileService, BCryptPasswordEncoder bCryptPasswordEncoder, @Lazy NotificationService notificationService) {
        this.userRepository = userRepository;
        this.awsFileService = awsFileService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.notificationService = notificationService;
    }

    @Override
    public List<User> getWholeUsers() {
        try {
            List<User> users = userRepository.findAll();
            log.info("Successfully got Whole Users");
            return users;
        } catch (Exception e) {
            log.error("Failed to get Whole Users", e);
            throw e;
        }
    }

    @Override
    public User getUserById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("User IDs must not be null");
        }

        try {
            Optional<User> u = userRepository.findById(id);
            if (u.isPresent()) {
                log.info("Successfully found user with id: {}", id);
                return u.get();
            }
            throw new UserNotFoundException("User not found with id: " + id);
        } catch (Exception e) {
            log.error("Failed to find user with id: {}", id);
            throw e;
        }
    }

    @Override
    public User getUserByEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("User email must not be null");
        }

        try {
            Optional<User> u = userRepository.findByEmail(email);
            if (u.isPresent()) {
                log.info("Successfully found user with email: {}", email);
                return u.get();
            }
            throw new UserNotFoundException("User not found with email: " + email);
        } catch (Exception e) {
            log.error("Failed to find user with email: {}", email);
            throw e;
        }
    }

    @Override
    public Boolean createUser(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new UserAlreadyExistsException("User already exists with this email");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(userDto.getPassword());

        User user = User.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .password(encodedPassword)
                .registrationMethod(RegistrationMethod.Otp)
                .log(100L)
                .likeCountThisWeek(0L)
                .build();
        userRepository.save(user);
        log.info("Successfully created user");
        return true;
    }

    @Override
    public Boolean updateUser(Long userId, String username, String description, MultipartFile profilePicture) {
        User existingUser = getUserById(userId);
        try {
            String profilePictureUrl = "";

            if (profilePicture != null) {
                awsFileService.removeProfileFile(existingUser.getEmail(), existingUser.getProfilePicture());
                profilePictureUrl = awsFileService.uploadProfileFile(profilePicture, existingUser.getEmail());
                existingUser.setProfilePicture(profilePictureUrl);
            }

            existingUser.setUsername(username);
            existingUser.setDescription(description);

            log.info("Successfully updated user with id: {}", userId);

            return true;
        } catch (Exception e) {
            log.error("Failed to update user with id: {}", userId, e);
            throw e;
        }
    }

    @Override
    public void deleteUserById(Long id) {
        try {
            userRepository.deleteById(id);
            log.info("Successfully deleted user with id: {}", id);
        } catch (Exception e) {
            log.error("Failed to delete user with id: {}", id, e);
            throw e;
        }
    }

    @Override
    public Boolean authenticateUser(String email, String password) {
        User user = getUserByEmail(email);
        if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
            log.info("Authentication succeeded");
            return true;
        }
        else {
            log.info("Authentication failed");
            return false;
        }
    }

    @Override
    public Page<User> getWholeUserByPage(Pageable pageable) {
        try {
            Page<User> users = userRepository.findAllByOrderByCreatedAtDesc(pageable);
            log.info("Successfully got whole users by page");
            return users;
        } catch (Exception e) {
            log.error("Failed to get whole users by page", e);
            throw e;
        }
    }

    @Override
    public Boolean updateUserByAdmin(AdminUpdateUserDto adminUpdateUserDto) {
        Long userId = adminUpdateUserDto.getUserId();
        User existingUser = getUserById(userId);
        try {
            Boolean isUpdated = false;
            if (!Objects.equals(existingUser.getLog(), adminUpdateUserDto.getLog())) {
                existingUser.setLog(adminUpdateUserDto.getLog());
                isUpdated = true;
            }
            if (!Objects.equals(existingUser.getRole(), adminUpdateUserDto.getRole())) {
                existingUser.setRole(adminUpdateUserDto.getRole());
                isUpdated = true;
            }
            if (!Objects.equals(existingUser.getIsLocked(), adminUpdateUserDto.getIsLocked())) {
                existingUser.setIsLocked(adminUpdateUserDto.getIsLocked());
                isUpdated = true;
            }

            if (isUpdated) userRepository.save(existingUser);

            log.info("Successfully updated user with id: {}", userId);

            return true;
        } catch (Exception e) {
            log.error("Failed to update user with id: {}", userId, e);
            throw e;
        }
    }

    @Override
    public List<User> getTop10UsersByLikesThisWeek() {
        try {
            Pageable pageable = PageRequest.of(0, 10);
            List<User> top10Users = userRepository.findTop10ByThisWeekLikedPosts(pageable);
            log.info("Successfully get top 10 users by likes this week");
            return top10Users;
        } catch (Exception e) {
            log.error("Failed to get top 10 users by likes this week");
            throw e;
        }
    }

    @Override
    public Boolean provideLogThisWeek(List<User> users) {
        try {
            Map<Integer, Long> logRewards = Map.of(
                    1, 100L,
                    2, 90L,
                    3, 80L,
                    4, 30L,
                    5, 30L,
                    6, 30L,
                    7, 30L,
                    8, 30L,
                    9, 20L,
                    10, 10L
            );

            AtomicInteger index = new AtomicInteger(1);
            users.forEach(user -> {
                int i = index.getAndIncrement();

                Long reward = logRewards.getOrDefault(i, 10L);
                user.setLog(user.getLog() + reward);

//                user.setLikeCountThisWeek(0L);

                String message = String.format("축하드립니다! 🏆 이주에 %d위를 하셔서 %s로그가 지급 됐습니다!", i, reward);
                notificationService.createNotification("이주의 순위", message, user);
            });

            userRepository.saveAll(users);
            userRepository.resetLikeCountThisWeek();
            log.info("Successfully provided log this week");
            return true;
        } catch (Exception e) {
            log.error("Failed to provide log this week", e);
            throw e;
        }
    }

    @Override
    public Boolean viewSecretPost(Long viewerId, BlogPost post) {
        User viewer = getUserById(viewerId);
        User owner = post.getUser();
        try {
            if (viewer.getLog() >= 10) {
                viewer.setLog(viewer.getLog() - 10);
                owner.setLog(owner.getLog() + 5);
                viewer.getPaidPosts().add(post);

                String message = String.format("%s님이 '%s' 글을 조회 하셨습니다.", viewer.getUsername(), post.getTitle());
                notificationService.createNotification("비밀글 조회", message, owner);

                userRepository.save(viewer);
                userRepository.save(owner);

                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("Failed to view secret post", e);
            throw e;
        }
    }

    @Override
    public Boolean checkIsUserPaid(Long viewerId, Post post) {
        User viewer = getUserById(viewerId);
        try {
            List<BlogPost> paidPosts = viewer.getPaidPosts();
            List<Long> paidPostIds = paidPosts.stream().map(Post::getId).collect(Collectors.toList());
            if (paidPostIds.contains(post.getId())) {
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("Failed to check is user paid", e);
            throw e;
        }
    }

    @Override
    public void addLikeCountThisWeek(Post post) {
        try {
            User user = post.getUser();
            user.setLikeCountThisWeek(user.getLikeCountThisWeek() + 1);
            userRepository.save(user);
            log.info("Successfully added like count this week");
        } catch (Exception e) {
            log.error("Failed to add like count this week");
        }
    }

    @Override
    public void removeLikeCountThisWeek(Post post) {
        try {
            User user = post.getUser();
            if (user.getLikeCountThisWeek() > 0) user.setLikeCountThisWeek(user.getLikeCountThisWeek() - 1);
            userRepository.save(user);
            log.info("Removed like for Post {} by User {}", post.getId(), user.getId());
        } catch (Exception e) {
            log.error("Failed to remove like count this week", e);
        }
    }

//    @Override
//    public void removeLikedPostContaining(Post post) {
//        try {
//            List<User> users = userRepository.findAllByThisWeekLikedPostsContaining(post.getId());
//            for (User user : users) {
//                user.getThisWeekLikedPosts().remove(post);
//                userRepository.save(user);
//            }
//            log.info("Successfully removed liked post containing");
//        } catch (Exception e) {
//            log.error("Failed to remove liked post containing", e);
//            throw e;
//        }
//    }

    @Override
    public void removePaidPostContaining(Post post) {
        try {
            List<User> users = userRepository.findAllByPaidPostsContaining(post.getId());
            for (User user : users) {
                user.getPaidPosts().remove(post);
                userRepository.save(user);
            }
            log.info("Successfully removed paid post containing");
        } catch (Exception e) {
            log.error("Failed to remove paid post containing", e);
            throw e;
        }
    }

    @Override
    public void updateUserLog(User user, Long logCount) {
        if (user == null) {
            log.error("Cannot update log count: User is null");
            throw new IllegalArgumentException("User cannot be null");
        }

        if (logCount == null || logCount < 0) {
            log.error("Invalid log count provided: {}", logCount);
            throw new IllegalArgumentException("Log count must be non-negative");
        }

        try {
            user.setLog(logCount);
            userRepository.save(user);
            log.info("Successfully updated log count for user ID {} to {}", user.getId(), logCount);
        } catch (Exception e) {
            log.error("Failed to update log count for user ID {} with log count {}", user.getId(), logCount, e);
            throw e;
        }
    }

    @Override
    public Boolean updateAttendance(Long userId) {
        User user = getUserById(userId);
        try {
            user.setLastAttendanceTime(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Australia/Sydney"))));
            user.setLog(user.getLog() + 10);
            return true;
        } catch (Exception e) {
            log.error("Failed to check attendance", e);
            throw e;
        }
    }

}
