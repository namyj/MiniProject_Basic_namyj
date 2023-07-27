package com.example.mutsamarket.jwt;

import lombok.Data;

// 발급받은 jwt 토큰을 전달하는 응답 형식
@Data
public class JwtTokenDto {
    private String token;
}
