package com.cb.service;

import com.cb.model.Comment;
import com.cb.model.MenuItem;
import com.cb.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceIlpl {
    private final CommentRepository commentRepository;
    public CommentServiceIlpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
    public void addComment(Comment comment) {
        commentRepository.save(comment);
    }

    public List<Comment> getCommentsByMenuItem(MenuItem menuItem) {
        return commentRepository.findByMenuItem(menuItem);
    }

}
