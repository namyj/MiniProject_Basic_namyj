package com.example.mutsamarket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // 테스트를 위해 csrf disable 시킴
            .authorizeHttpRequests(
                    authHttp -> authHttp
                            .requestMatchers("/no-auth", "/users/login").permitAll()
                            .requestMatchers("/", "/users/register").anonymous()
                            .anyRequest().authenticated()
            );

        http.formLogin(
                formLogin -> formLogin
                        .loginPage("/users/login")
                        .defaultSuccessUrl("/users/my-profile")
                        .failureUrl("/users/login?fail")
                        .permitAll()
        );

        return http.build();
    }

    // 로그인 Test를 위해 UserDetailsManager에 테스트 사용자 저장
    // @Bean
    // public UserDetailsManager getUserDetailsManager(PasswordEncoder passwordEncoder) {
    //     UserDetails testuser = User.withUsername("admin")
    //             .password(passwordEncoder.encode("1234"))
    //             .build();
    //     return new InMemoryUserDetailsManager(testuser);
    // }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();

    }
}
