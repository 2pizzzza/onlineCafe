package com.cb.dto;

public class CommentDto {
    private Long userId;
    private String text;

    public CommentDto(Long userId, String text) {
        this.userId = userId;
        this.text = text;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
