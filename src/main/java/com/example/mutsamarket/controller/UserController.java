package com.example.mutsamarket.controller;

import com.example.mutsamarket.dto.ResponseDto;
import com.example.mutsamarket.jwt.JwtRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.mutsamarket.entity.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    // 로그인 form
    // @GetMapping("/login")
    // public String loginForm() {
    //     return "login-form";
    // }

    // 로그인 성공 시 token 발급 엔드 포인트
    @PostMapping("/login")
    public String login(
            @RequestBody JwtRequestDto requestDto
    ) {
        return null;
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

    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    public UserController(
            UserDetailsManager userDetailsManager,
            PasswordEncoder passwordEncoder
    ) {
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
    }

    // 회원가입 form
    @PostMapping("/register")
    public ResponseDto registerPost(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("password-check") String passwordCheck,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "address", required = false) String address
    ) {
        ResponseDto response = new ResponseDto();

        if (!password.equals(passwordCheck)) {
            log.warn("Password does not match...");
            response.setMessage("회원가입에 실패했습니다.");
            return response;
            // return "redirect:/users/register?error";
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

            response.setMessage("회원가입에 성공했습니다");
            return response;
        } catch (Exception e) {
            log.error(e.toString());
            response.setMessage("회원가입에 실패했습니다.");
            return response;
        }

    }
}
