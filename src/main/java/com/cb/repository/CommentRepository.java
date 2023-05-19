package com.cb.repository;

import com.cb.model.Comment;
import com.cb.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByMenuItem(MenuItem menuItem);
}
