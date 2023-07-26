package com.example.mutsamarket.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/users")
public class UserController {
    @GetMapping("/register")
    public String registerForm() {
        // return "register page";
        return "register-form";
    }

    @PostMapping("/register")
    public void registerPost(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("password-check") String passwordCheck,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("address") String address
    ) {
        log.info("username = " + username);
        log.info("password = " + password);
        log.info("passwordCheck = " + passwordCheck);
        log.info("email = " + email);
        log.info("phone = " + phone);
        log.info("address = " + address);
    }
}
