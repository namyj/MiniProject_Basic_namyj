package com.example.mutsamarket.jwt;

import lombok.Data;

// jwt 토큰을 요청하는 요청 형식
@Data
public class JwtRequestDto {
    private String username;
    private String password;
}
