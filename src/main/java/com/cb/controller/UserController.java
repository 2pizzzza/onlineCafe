package com.cb.controller;

import com.cb.dto.UserDto;
import com.cb.model.User;
import com.cb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String registrationForm(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "user";
    }

    @PostMapping("/updatePhoto")
    public String updatePhoto(@RequestParam("email") String email,
                              @RequestParam("photo") MultipartFile photo) {
        try {
            userService.updateUserPhoto(email, photo);
            return "redirect:/user/";
        } catch (IllegalArgumentException e) {
            // Обработка ошибки, когда пользователь не найден
            // Например, перенаправление на страницу с сообщением об ошибке
            return "error";
        }
    }
}

