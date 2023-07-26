package com.example.mutsamarket.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.mutsamarket.entity.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/users")
public class UserController {
    // 로그인
    @GetMapping("/login")
    public String loginForm() {
        return "login-form";
    }

    @GetMapping("/my-profile")
    public String myProfile(Authentication authentication) {
        log.info("Login success!");

        // 현재 로그인 한 사용자 정보 출력
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal(); // UserDetails 객체 반환

        log.info(userDetails.getUsername());
        log.info(userDetails.getEmail());
        return "my-profile";
    }

    // 회원가입
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
    public String registerPost(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("password-check") String passwordCheck,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("address") String address
    ) {
        if (!password.equals(passwordCheck)) {
            log.warn("Password does not match...");
            return "redirect:/users/register?error";
        }

        try {
            log.info("Password match!");
            UserDetails details = CustomUserDetails.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .email(email)
                    .phone(phone)
                    .address(address)
                    .build();

            userDetailsManager.createUser(details);

            return "redirect:/users/login";
        } catch (Exception e) {
            log.error(e.toString());
            return "redirect:/users/register?error";
        }

    }
}
