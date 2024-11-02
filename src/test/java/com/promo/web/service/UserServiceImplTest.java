package com.promo.web.service;

import com.promo.web.entity.Role;
import com.promo.web.entity.User;
import com.promo.web.exception.PostNotFoundException;
import com.promo.web.exception.UserNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Test
    void getWholeUsers() {
        User user1 = User.builder().email("user1@gmail.com").username("user1").password("1234").role(Role.USER).build();
        User user2 = User.builder().email("user2@gmail.com").username("user2").password("1234").role(Role.ADMIN).build();

        userService.createUser(user1);
        userService.createUser(user2);

        List<User> wholeUsers = userService.getWholeUsers();
        assertNotNull(wholeUsers);
        assertTrue(wholeUsers.size() == 2);
    }

    @Test
    void createUser() {
        User user1 = User.builder()
                .email("user1@gmail.com")
                .username("user1")
                .password("1234")
                .role(Role.USER)
                .build();

        userService.createUser(user1);
        User createdUser = userService.getUserById(user1.getId());

        assertEquals("user1@gmail.com", createdUser.getEmail());
        assertEquals("user1", createdUser.getUsername());
        assertEquals("1234", createdUser.getPassword());
        assertEquals(Role.USER, createdUser.getRole());
    }

    @Test
    void updateUser() {
        User user1 = User.builder().email("user1@gmail.com").username("user1").password("1234").role(Role.USER).build();
        userService.createUser(user1);
        User createdUser = userService.getUserById(user1.getId());

        User user2 = User.builder().email("user2@gmail.com").username("user2").password("1234").role(Role.ADMIN).build();
        userService.updateUser(createdUser.getId(), user2);

        User updatedUser = userService.getUserById(user1.getId());
        assertEquals("user2@gmail.com", updatedUser.getEmail());
        assertEquals("user2", updatedUser.getUsername());
        assertEquals("1234", updatedUser.getPassword());
        assertEquals(Role.ADMIN, updatedUser.getRole());
    }

    @Test
    void deleteUserById() {
        User user1 = User.builder().email("user1@gmail.com").username("user1").password("1234").role(Role.USER).build();
        userService.createUser(user1);
        User createdUser = userService.getUserById(user1.getId());
        userService.deleteUserById(createdUser.getId());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(createdUser.getId());
        });
        assertEquals("User not found with id: " + createdUser.getId(), exception.getMessage());
    }
}