package com.cb.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Cafe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String disription;
    private String adress;

    @OneToMany(mappedBy = "cafe", cascade = CascadeType.ALL)
    private List<MenuItem> menus;

    public Cafe(Long id, String name, String disription, String adress, List<MenuItem> menus) {
        this.id = id;
        this.name = name;
        this.disription = disription;
        this.adress = adress;
        this.menus = menus;
    }
    public Cafe() {
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

    public String getDisription() {
        return disription;
    }

    public void setDisription(String disription) {
        this.disription = disription;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public List<MenuItem> getMenus() {
        return menus;
    }

    public void setMenus(List<MenuItem> menus) {
        this.menus = menus;
    }
}
