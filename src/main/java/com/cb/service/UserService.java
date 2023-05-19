package com.cb.service;

import com.cb.dto.UserDto;
import com.cb.model.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    void saveUser(UserDto userDto);

    void updateUserPhoto(String email, MultipartFile photo);

    User findUserByEmail(String email);
}
