package com.promo.web.service;

import com.promo.web.dto.UserDto;
import com.promo.web.entity.User;
import com.promo.web.exception.UserAlreadyExistsException;
import com.promo.web.exception.UserNotFoundException;
import com.promo.web.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
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
                .build();
        userRepository.save(user);
        log.info("Successfully created user");
        return true;
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
}
