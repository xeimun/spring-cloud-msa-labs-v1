package com.sesac.userservice.service;

import com.sesac.userservice.entity.User;
import com.sesac.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User not found wuth id" + id)
        );
    }
}
