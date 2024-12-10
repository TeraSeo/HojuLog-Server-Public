package com.hojunara.web.service;

import com.hojunara.web.dto.request.UserDto;
import com.hojunara.web.entity.User;

import java.util.List;

public interface UserService {
    List<User> getWholeUsers();

    User getUserById(Long id);

    User getUserByEmail(String email);

    Boolean createUser(UserDto userDto);

    void updateUser(Long id, User user);

    void deleteUserById(Long id);

    Boolean authenticateUser(String email, String password);
}
