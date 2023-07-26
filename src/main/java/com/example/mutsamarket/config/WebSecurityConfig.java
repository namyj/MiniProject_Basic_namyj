package com.example.mutsamarket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // 테스트를 위해 csrf disable 시킴
                .authorizeHttpRequests(
                        authHttp -> authHttp
                                .requestMatchers("/no-auth").permitAll()
                                .requestMatchers("/", "/users/register").anonymous()
                                .anyRequest().authenticated()
                );

        return http.build();
    }
}
