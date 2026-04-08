package com.ionidea.mothramxbe.controller;


import lombok.*;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;

import com.ionidea.mothramxbe.dto.AuthRequest;
import com.ionidea.mothramxbe.security.JwtUtil;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public String login(@RequestBody AuthRequest request) {

        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        return jwtUtil.generateToken(request.getEmail());
    }
}