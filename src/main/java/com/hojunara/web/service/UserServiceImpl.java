package com.hojunara.web.service;

import com.hojunara.web.aws.s3.AwsFileService;
import com.hojunara.web.dto.request.AdminUpdateUserDto;
import com.hojunara.web.dto.request.UserDto;
import com.hojunara.web.entity.RegistrationMethod;
import com.hojunara.web.entity.User;
import com.hojunara.web.exception.UserAlreadyExistsException;
import com.hojunara.web.exception.UserNotFoundException;
import com.hojunara.web.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

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
                .log(0L)
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
            if (!Objects.equals(existingUser.getLikeCountThisWeek(), adminUpdateUserDto.getLikeCountThisWeek())) {
                existingUser.setLikeCountThisWeek(adminUpdateUserDto.getLikeCountThisWeek());
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
            List<User> top10Users = userRepository.findTop10ByOrderByLikeCountThisWeekDesc();
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
                    8, 30L,
                    9, 20L
            );

            log.info("user counts: " + users.size() + "!!!!!!!!!!!!!!!!!!!");

            AtomicInteger index = new AtomicInteger(1);
            users.forEach(user -> {
                int i = index.getAndIncrement();

                Long reward = logRewards.getOrDefault(i, 10L);
                user.setLog(user.getLog() + reward);

                user.setLikeCountThisWeek(0L);

                String message = String.format("축하드립니다! 🏆 이주에 %d위를 하셔서 %s로그가 지급 됐습니다!", i, reward);
                notificationService.createNotification("이주의 순위", message, user);
            });

            userRepository.saveAll(users);
            log.info("Successfully provided log this week");
            return true;
        } catch (Exception e) {
            log.error("Failed to provide log this week", e);
            throw e;
        }
    }
}
