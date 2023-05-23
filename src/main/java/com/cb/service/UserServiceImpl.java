package com.cb.service;


import com.cb.dto.UserDto;
import com.cb.model.Role;
import com.cb.model.User;
import com.cb.repository.RoleRepository;
import com.cb.repository.UserRepository;
import com.cb.util.TbConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public void saveUser(UserDto userDto) {
        Role role = roleRepository.findByName(TbConstants.Roles.USER);

        if (role == null)
            role = roleRepository.save(new Role(TbConstants.Roles.USER));

        User user = new User(userDto.getName(), userDto.getEmail(), passwordEncoder.encode(userDto.getPassword()),
                Arrays.asList(role));
        userRepository.save(user);
    }

    @Override
    public void updateUserPhoto(String email, MultipartFile photo) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        try {
            String photoPath = savePhoto(photo);
            user.setPhoto(photoPath);
            userRepository.save(user);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save photo", e);
        }
    }

    private String savePhoto(MultipartFile photo) throws IOException {
        String fileName = UUID.randomUUID().toString() + "-" + StringUtils.cleanPath(photo.getOriginalFilename());
        String uploadDir = "com/cb/util/static/media/";
        String filePath = uploadDir + fileName;

        Path directory = Paths.get(uploadDir);
        if (Files.notExists(directory)) {
            Files.createDirectories(directory);
        }

        Path fullPath = directory.resolve(fileName);
        try (OutputStream outputStream = Files.newOutputStream(fullPath)) {
            outputStream.write(photo.getBytes());
        }

        return "/" + filePath;
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public User getCurrentUser() {
        // Внутри этой функции предполагается, что есть доступ к объекту userService, который реализует UserService

        // Проверяем, есть ли текущий аутентифицированный пользователь
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null; // Пользователь не аутентифицирован
        }

        // Получаем email текущего пользователя
        String email = authentication.getName();

        // Используем userService для найти пользователя по email
        User user = findUserByEmail(email);
        if (user == null) {
            throw new RuntimeException("Failed to get current user"); // Обработка ошибки, если пользователь не найден
        }

        return user;
    }
}
