package com.promo.web.service;

import com.promo.web.entity.User;
import com.promo.web.exception.UserNotFoundException;
import com.promo.web.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
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
    public void createUser(User user) {
        try {
            userRepository.save(user);
            log.info("Successfully created user");
        } catch (Exception e) {
            log.error("Failed to create user", e);
            throw e;
        }
    }

    @Override
    public void updateUser(Long id, User user) {
        User existingUser = getUserById(id);

        try {
            existingUser.setEmail(user.getEmail());
            existingUser.setUsername(user.getUsername());
            existingUser.setPassword(user.getPassword());
            existingUser.setRole(user.getRole());

            userRepository.save(existingUser);
            log.info("Successfully updated user with id: {}", id);
        } catch (Exception e) {
            log.error("Failed to update user with id: {}", id, e);
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
}
