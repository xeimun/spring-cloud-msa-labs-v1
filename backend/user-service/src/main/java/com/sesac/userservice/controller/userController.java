package com.sesac.userservice.controller;

import com.sesac.userservice.entity.User;
import com.sesac.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class userController {
    
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable long id) {
        try {
            User user = userService.findById(id);
            return ResponseEntity.ok(user);

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

    }
}
