package com.cb.controller;

import com.cb.model.*;
import com.cb.repository.CafeRepository;
import com.cb.repository.CartRepository;
import com.cb.repository.MenuItemRepository;
import com.cb.repository.UserRepository;
import com.cb.service.CartService;
import com.cb.service.CommentServiceIlpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class MenuItemController {
    @Autowired
    private CafeRepository cafeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MenuItemRepository menuRepository;

    @Autowired
    private CommentServiceIlpl commentServiceIlpl;

    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepository cartRepository;

    @GetMapping("/menu/{cafeId}")
    public String getMenuAndCart(@PathVariable("cafeId") Long cafeId,
                                 @RequestParam(value = "category", required = false) String category,
                                 Principal principal,
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

            String email = principal.getName();
            User user = userRepository.findByEmail(email);
            if (user != null) {
                Cart cart = cartService.getCartByUser(user);
                if (cart != null) {
                    List<CartItem> cartItems = cart.getItems().stream()
                            .map(menuItem -> new CartItem(menuItem, 1)) // Здесь 1 - это количество позиций, можно настроить по вашему требованию
                            .collect(Collectors.toList());
                    model.addAttribute("cartItems", cartItems);
                }
            }
            return "menu.html";
        } else {
            return "error";
        }
    }

    @GetMapping("/menu/{cafeId}/dish/{menuId}")
    public String getMenuDish(@PathVariable("cafeId") Long cafeId,
                              @PathVariable("menuId") Long menuId,
                              Model model,
                              Principal principal) {
        Optional<Cafe> cafe = cafeRepository.findById(cafeId);
        Optional<MenuItem> menuItem = menuRepository.findById(menuId);
        if (cafe.isPresent() && menuItem.isPresent()) {
            model.addAttribute("cafe", cafe.get());
            model.addAttribute("menu", menuItem.get());
            model.addAttribute("comment", new Comment());

            List<Comment> comments = commentServiceIlpl.getCommentsByMenuItem(menuItem.get());
            model.addAttribute("comments", comments);

            String email = principal.getName();
            User user = userRepository.findByEmail(email);
            if (user != null) {
                Cart cart = cartService.getCartByUser(user);
                model.addAttribute("cart", cart);
            }

            return "dish.html";
        } else {
            return "error";
        }
    }

    @PostMapping("/menu/{cafeId}/dish/{menuId}/add-to-cart")
    public String addToCart(@PathVariable("cafeId") Long cafeId,
                            @PathVariable("menuId") Long menuId,
                            Principal principal) {
        Optional<Cafe> cafe = cafeRepository.findById(cafeId);
        Optional<MenuItem> menuItem = menuRepository.findById(menuId);
        if (cafe.isPresent() && menuItem.isPresent()) {
            String email = principal.getName();
            User user = userRepository.findByEmail(email);
            if (user != null) {
                Cart cart = cartService.getCartByUser(user);
                if (cart == null) {
                    cart = new Cart(); // Создание новой корзины
                    cart.setUser(user);
                }
                cartService.addItemToCart(cart, menuItem.get());
            }
            return "redirect:/menu/" + cafeId;
        } else {
            return "error";
        }
    }

    @PostMapping("/menu/{cafeId}/dish/{menuId}/remove-from-cart")
    public String removeFromCart(@PathVariable("cafeId") Long cafeId,
                                 @PathVariable("menuId") Long menuId,
                                 Principal principal) {
        Optional<Cafe> cafe = cafeRepository.findById(cafeId);
        Optional<MenuItem> menuItem = menuRepository.findById(menuId);
        if (cafe.isPresent() && menuItem.isPresent()) {
            String email = principal.getName();
            User user = userRepository.findByEmail(email);
            if (user != null) {
                Cart cart = cartService.getCartByUser(user);
                cartService.removeItemFromCart(cart, menuItem.get());
            }
            return "redirect:/menu/" + cafeId;
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

    @GetMapping("/cart/remove/{itemId}")
    public String removeFromCart(@PathVariable("itemId") Long itemId, Principal principal, HttpServletRequest request) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        if (user != null) {
            Cart cart = cartService.getCartByUser(user);
            if (cart != null) {
                Optional<MenuItem> menuItem = menuRepository.findById(itemId);
                if (menuItem.isPresent()) {
                    cartService.removeItemFromCart(cart, menuItem.get());
                }
            }
        }

        String previousUrl = request.getHeader("Referer");

        return "redirect:" + previousUrl;
    }

    @GetMapping("/cart")
    public String viewCart(Principal principal, Model model) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        if (user != null) {
            Cart cart = cartService.getCartByUser(user);
            if (cart != null) {
                List<CartItem> cartItems = cart.getItems().stream()
                        .map(menuItem -> new CartItem(menuItem, 1)) // Здесь 1 - это количество позиций, можно настроить по вашему требованию
                        .collect(Collectors.toList());

                double total = cartItems.stream()
                        .mapToDouble(cartItem -> cartItem.getMenuItem().getPrice().doubleValue())
                        .sum();

                model.addAttribute("cartItems", cartItems);
                model.addAttribute("total", total); // Добавляем переменную total в модель
                return "cart";
            }
        }
        return "error";
    }


}