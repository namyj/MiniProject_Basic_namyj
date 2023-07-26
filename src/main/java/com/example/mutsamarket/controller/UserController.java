package com.example.mutsamarket.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/users")
public class UserController {

    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    public UserController(
            UserDetailsManager userDetailsManager,
            PasswordEncoder passwordEncoder
    ) {
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String registerForm() {
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
        if (password.equals(passwordCheck)) {
            log.info("Password match!");
        }

        log.warn("Password does not match...");
    }
}
