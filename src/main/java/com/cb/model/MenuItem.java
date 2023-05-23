package com.cb.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String category;
    private BigDecimal price;

    private String photo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id")
    private Cafe cafe;
    @OneToMany(mappedBy = "menuItem", cascade = CascadeType.ALL)
    private List<Comment> comments;

    public MenuItem(Long id, String name, String category, BigDecimal price, Cafe cafe,List<Comment> comments, String photo) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.cafe = cafe;
        this.comments = comments;
        this.photo = photo;
    }
    public MenuItem(){

    }
    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setMenuItem(this);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setMenuItem(null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Cafe getCafe() {
        return cafe;
    }

    public void setCafe(Cafe cafe) {
        this.cafe = cafe;
    }
}
