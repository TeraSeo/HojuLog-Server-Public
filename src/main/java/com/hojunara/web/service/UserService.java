package com.hojunara.web.service;

import com.hojunara.web.dto.request.UpdateUserDto;
import com.hojunara.web.dto.request.UserDto;
import com.hojunara.web.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    List<User> getWholeUsers();

    User getUserById(Long id);

    User getUserByEmail(String email);

    Boolean createUser(UserDto userDto);

    Boolean updateUser(Long userId, String username, String description, MultipartFile profilePicture);

    void deleteUserById(Long id);

    Boolean authenticateUser(String email, String password);
}
