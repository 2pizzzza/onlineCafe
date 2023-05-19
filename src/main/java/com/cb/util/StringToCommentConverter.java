package com.cb.util;

import com.cb.model.Comment;
import org.springframework.core.convert.converter.Converter;

public class StringToCommentConverter implements Converter<String, Comment> {

    @Override
    public Comment convert(String source) {
        Comment comment = new Comment();
        comment.setComment(source);
        return comment;
    }
}
