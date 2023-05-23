package com.cb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutUs {

    @GetMapping("/about")
    public String getAboutUs(){
        return "about-us";
    }
}
