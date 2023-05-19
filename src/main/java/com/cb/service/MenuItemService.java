package com.cb.service;

import com.cb.dto.CommentDto;
import com.cb.model.Comment;
import com.cb.model.MenuItem;
import com.cb.model.User;
import com.cb.repository.CommentRepository;
import com.cb.repository.MenuItemRepository;
import com.cb.repository.UserRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public MenuItemService(MenuItemRepository menuItemRepository, CommentRepository commentRepository, UserRepository userRepository) {
        this.menuItemRepository = menuItemRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

}
