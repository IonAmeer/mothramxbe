package com.ionidea.mothramxbe.security.controller;

import lombok.RequiredArgsConstructor;

import com.ionidea.mothramxbe.security.dto.AuthRequest;
import com.ionidea.mothramxbe.security.dto.AuthResponseDTO;
import com.ionidea.mothramxbe.security.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController { // all controllers must return response entity

    private final AuthService authService;

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody AuthRequest request) {
        return authService.login(request);
    }

}