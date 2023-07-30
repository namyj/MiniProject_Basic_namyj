package com.example.mutsamarket.service;

import com.example.mutsamarket.entity.CustomUserDetails;
import com.example.mutsamarket.entity.UserEntity;
import com.example.mutsamarket.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@Service
public class JpaUserDetailsManager implements UserDetailsManager {

    private final UserRepository userRepository;

    public JpaUserDetailsManager(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;

        // 로그인 테스트를 위해 테스트 사용자 생성
        createUser(CustomUserDetails.builder()
                .username("user")
                .password(passwordEncoder.encode("1234"))
                .email("test@email.com")
                .phone("010-1234-1234")
                .address("Seoul")
                .build()
        );
    }

    @Override
    // 새로운 사용자 생성
    public void createUser(UserDetails user) {
        log.info("Create new user: {}", user.getUsername());
        
        // 이미 존재하는 사용자
        if (this.userExists(user.getUsername()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        try {
            userRepository.save(((CustomUserDetails) user).newEntity());
        } catch (Exception e) {
            log.error(e.toString());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    // 해당 이름을 가진 사용자가 존재하는지 확인
    public boolean userExists(String username) {
        log.info("Check if user: {} exists", username);
        return this.userRepository.existsByUsername(username);
    }

    // 반드시 구현
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty())
            throw new UsernameNotFoundException(username);

        return CustomUserDetails.fromEntity(optionalUser.get());
    }

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }
    

}
