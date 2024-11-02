package com.promo.web.service;

import com.promo.web.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getWholeUsers();

    User getUserById(Long id);

    void createUser(User user);

    void updateUser(Long id, User user);

    void deleteUserById(Long id);

}
