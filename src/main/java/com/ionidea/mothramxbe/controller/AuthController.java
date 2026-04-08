package com.ionidea.mothramxbe.controller;

import lombok.*;
import org.springframework.web.bind.annotation.*;

import com.ionidea.mothramxbe.dto.AuthRequest;
import com.ionidea.mothramxbe.dto.AuthResponseDTO;
import com.ionidea.mothramxbe.security.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody AuthRequest request) {
        return authService.login(request);
    }

}