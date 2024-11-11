package com.promo.web.service;

import com.promo.web.dto.UserDto;
import com.promo.web.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getWholeUsers();

    User getUserById(Long id);

    User getUserByEmail(String email);

    Boolean createUser(UserDto userDto);

    void updateUser(Long id, User user);

    void deleteUserById(Long id);

    Boolean authenticateUser(String email, String password);
}
