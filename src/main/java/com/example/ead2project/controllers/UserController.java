package com.example.ead2project.controllers;

import com.example.ead2project.serviceInterface.Dto.UserServiceDto;
import com.example.ead2project.serviceInterface.interfaces.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
       
    private final IUserService userService;

    @GetMapping("/whoami")
    public ResponseEntity<UserServiceDto> get() {
        return userService.getUserByEmail()
            .<ResponseEntity<UserServiceDto>>thenApply(user -> user != null ? 
                ResponseEntity.ok(user) : 
                ResponseEntity.notFound().build())
            .join();
    }
} 