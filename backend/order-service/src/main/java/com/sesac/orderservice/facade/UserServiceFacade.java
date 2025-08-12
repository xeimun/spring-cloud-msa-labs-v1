package com.sesac.orderservice.facade;

import com.sesac.orderservice.client.UserServiceClient;
import com.sesac.orderservice.client.dto.UserDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceFacade {

    private final UserServiceClient userServiceClient;

    @CircuitBreaker(name = "user-service", fallbackMethod = "getUserFallback")
    public UserDto getUserWithFallback(Long userId) {
        log.info("User service 호출 시도 - userId = {}", userId);
        return userServiceClient.getUserById(userId);
    }

    public UserDto getUserFallback(Long userId, Throwable ex) {

        log.warn("User Service 장애 감지! Fallback 실행 - userId = {}", userId, ex);

        UserDto defaultUser = new UserDto();
        defaultUser.setId(userId);
        defaultUser.setName("임시 사용자");
        defaultUser.setEmail("temp.@example.com");

        return defaultUser;
    }
}
