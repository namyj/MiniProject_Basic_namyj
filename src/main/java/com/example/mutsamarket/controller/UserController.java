package com.example.mutsamarket.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/users")
public class UserController {
    @GetMapping("/login")
    public String loginForm() {
        return "login-form";
    }

    @GetMapping("/my-profile")
    public String myProfile() {
        log.info("Login success!");
        return "my-profile";
    }
}
