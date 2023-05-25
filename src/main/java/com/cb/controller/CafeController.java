package com.cb.controller;

import com.cb.model.Cafe;
import com.cb.repository.CafeRepository;
import com.cb.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.awt.*;
import java.util.List;
import java.util.Optional;

@Controller
public class    CafeController {
    @Autowired
    private CafeRepository cafeRepository;

    @GetMapping("/cafes")
    public String getCafes(Model model) {
        List<Cafe> cafes = cafeRepository.findAll();
        model.addAttribute("cafes", cafes);
        return "cafes.html";
    }
}

