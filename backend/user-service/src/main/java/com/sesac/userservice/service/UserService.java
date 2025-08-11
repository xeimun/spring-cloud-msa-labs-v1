package com.sesac.userservice.service;

import com.sesac.userservice.dto.LoginRequest;
import com.sesac.userservice.dto.LoginResponse;
import com.sesac.userservice.entity.User;
import com.sesac.userservice.repository.UserRepository;
import com.sesac.userservice.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User not found with id" + id)
        );
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                                  .orElseThrow(
                                          () -> new RuntimeException("User not found with email" + request.getEmail())
                                  );

        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        String token = jwtTokenProvider.generateToken(user.getEmail(), user.getId());

        return new LoginResponse(token, user.getId(), user.getEmail(), user.getName());
    }
}
