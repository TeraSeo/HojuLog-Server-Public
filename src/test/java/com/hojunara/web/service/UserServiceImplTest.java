package com.hojunara.web.service;

import com.hojunara.web.dto.request.UserDto;
import com.hojunara.web.entity.Role;
import com.hojunara.web.entity.User;
import com.hojunara.web.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Test
    void getWholeUsers() {
        UserDto user1 = UserDto.builder().email("user1@gmail.com").username("user1").password("1234").build();
        UserDto user2 = UserDto.builder().email("user2@gmail.com").username("user2").password("1234").build();

        userService.createUser(user1);
        userService.createUser(user2);

        List<User> wholeUsers = userService.getWholeUsers();
        assertNotNull(wholeUsers);
        assertTrue(wholeUsers.size() == 2);
    }

    @Test
    void createUser() {
        UserDto user1 = UserDto.builder()
                .email("user1@gmail.com")
                .username("user1")
                .password("1234")
                .build();

        userService.createUser(user1);
        User createdUser = userService.getUserByEmail(user1.getEmail());

        assertEquals("user1@gmail.com", createdUser.getEmail());
        assertEquals("user1", createdUser.getUsername());
        assertEquals(Role.USER, createdUser.getRole());
    }

    @Test
    void updateUser() {
        UserDto user1 = UserDto.builder().email("user1@gmail.com").username("user1").password("1234").build();
        userService.createUser(user1);
        User createdUser = userService.getUserByEmail(user1.getEmail());

        User user2 = User.builder().email("user2@gmail.com").username("user2").password("1234").role(Role.ADMIN).build();
        userService.updateUser(createdUser.getId(), user2);

        User updatedUser = userService.getUserById(createdUser.getId());
        assertEquals("user2@gmail.com", updatedUser.getEmail());
        assertEquals("user2", updatedUser.getUsername());
        assertEquals("1234", updatedUser.getPassword());
        assertEquals(Role.ADMIN, updatedUser.getRole());
    }

    @Test
    void deleteUserById() {
        UserDto user1 = UserDto.builder().email("user1@gmail.com").username("user1").password("1234").build();
        userService.createUser(user1);
        User createdUser = userService.getUserByEmail(user1.getEmail());
        userService.deleteUserById(createdUser.getId());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(createdUser.getId());
        });
        assertEquals("User not found with id: " + createdUser.getId(), exception.getMessage());
    }
}