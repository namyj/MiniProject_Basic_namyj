package com.example.mutsamarket.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenUtils {
    private final Key signingKey; // 토큰 변조 여부를 확인하기 위한 키
    private final JwtParser jwtParser; // 토큰이 유효한지 확인하는 parser

    public JwtTokenUtils(@Value("${jwt.secret}") String jwtSecret) {
        this.signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        this.jwtParser = Jwts
                .parserBuilder()
                .setSigningKey(this.signingKey)
                .build();
    }

    // jwt이 유효한지 판단하는 메서드
    public boolean validateToken(String token) {
        try {
            jwtParser.parseClaimsJwt(token);
            return true;
        } catch (Exception e) {
            log.warn("invalid jwt: {}", e.getClass());
            return false;
        }
    }

    // jwt에서 사용자 정보를 추출하는 메서드
    public Claims parseClaims(String token) {
        return jwtParser
                .parseClaimsJwt(token)
                .getBody();
    }

    // jwt 생성 메서드
    public String generateToken(UserDetails userDetails) {
        // Claims: JWT에 담기는 정보의 단위, Map의 Key를 의미 
        Claims jwtClaims = Jwts.claims()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(3600)));

        return Jwts.builder()
                .setClaims(jwtClaims)
                .signWith(signingKey)
                .compact(); // 문자열로 반환
    }

}
