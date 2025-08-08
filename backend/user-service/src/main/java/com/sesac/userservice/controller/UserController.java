package com.sesac.userservice.controller;

import com.sesac.userservice.entity.User;
import com.sesac.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @Operation(summary = "유저 조회", description = "ID로 유저 정보를 조회합니다.")
    public ResponseEntity<User> getUser(@PathVariable long id) {
        try {
            User user = userService.findById(id);
            return ResponseEntity.ok(user);

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

    }
}
