package com.cb.controller;

import com.cb.model.Cafe;
import com.cb.model.Comment;
import com.cb.model.MenuItem;
import com.cb.model.User;
import com.cb.repository.CafeRepository;
import com.cb.repository.MenuItemRepository;
import com.cb.repository.UserRepository;
import com.cb.service.CommentServiceIlpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class MenuController {
    @Autowired
    private CafeRepository cafeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MenuItemRepository menuRepository;

    @Autowired
    private CommentServiceIlpl commentServiceIlpl;
    @GetMapping("/menu/{cafeId}")
    public String getMenu(@PathVariable("cafeId") Long cafeId,
                          @RequestParam(value = "category", required = false) String category,
                          Model model) {
        Optional<Cafe> cafe = cafeRepository.findById(cafeId);
        if (cafe.isPresent()) {
            List<MenuItem> menus = cafe.get().getMenus();
            List<String> categories = menuRepository.findDistinctCategories();
            if (category != null && !category.isEmpty()) {
                menus = menus.stream()
                        .filter(m -> m.getCategory().equals(category))
                        .collect(Collectors.toList());
            }

            model.addAttribute("cafe", cafe.get());
            model.addAttribute("menus", menus);
            model.addAttribute("categories", categories);
            return "menu.html";
        } else {
            return "error";
        }
    }
    @GetMapping("/menu/{cafeId}/dish/{menuId}")
    public String getMenuDish(@PathVariable("cafeId") Long cafeId,
                              @PathVariable("menuId") Long menuId,
                              Model model) {
        Optional<Cafe> cafe = cafeRepository.findById(cafeId);
        Optional<MenuItem> menuItem = menuRepository.findById(menuId);
        if (cafe.isPresent() && menuItem.isPresent()) {
            model.addAttribute("cafe", cafe.get());
            model.addAttribute("menu", menuItem.get());
            model.addAttribute("comment", new Comment());

            List<Comment> comments = commentServiceIlpl.getCommentsByMenuItem(menuItem.get());
            model.addAttribute("comments", comments);

            return "dish.html";
        } else {
            return "error";
        }
    }
    @PostMapping("/menu/{cafeId}/dish/{menuId}/comment")
    @PreAuthorize("hasRole('USER')")
    public String addComment(@PathVariable("cafeId") Long cafeId,
                             @PathVariable("menuId") Long menuId,
                             @ModelAttribute("comment") Comment comment,
                             Authentication authentication) {
        Optional<Cafe> cafe = cafeRepository.findById(cafeId);
        Optional<MenuItem> menuItem = menuRepository.findById(menuId);
        if (cafe.isPresent() && menuItem.isPresent()) {
            // Находим текущего пользователя
            String email = authentication.getName();
            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new IllegalStateException("User not found");
            }

            comment.setUser(user);
            comment.setMenuItem(menuItem.get());

            commentServiceIlpl.addComment(comment);

            return "redirect:/menu/" + cafeId + "/dish/" + menuId;
        } else {
            return "error";
        }
    }



}