package com.cb.controller;

import com.cb.model.Cafe;
import com.cb.model.User;
import com.cb.repository.CafeRepository;
import com.cb.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class MainPageController {

    private final UserService userService;
    private final CafeRepository cafeRepository;

    public MainPageController(UserService userService, CafeRepository cafeRepository) {
        this.userService = userService;
        this.cafeRepository = cafeRepository;
    }

    @GetMapping("/home")
    public String home(Model model, Principal principal) {
        String email = principal.getName();
        User user = userService.findUserByEmail(email);

        if (user != null) {
            model.addAttribute("userPhoto", user.getPhoto());
        }

        List<Cafe> cafes = cafeRepository.findAll();
        model.addAttribute("cafes", cafes);

        return "home.html";
    }
}
