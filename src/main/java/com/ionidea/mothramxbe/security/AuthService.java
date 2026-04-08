package com.ionidea.mothramxbe.security;

import com.ionidea.mothramxbe.dto.AuthRequest;
import com.ionidea.mothramxbe.dto.AuthResponseDTO;
import com.ionidea.mothramxbe.entity.Role;
import com.ionidea.mothramxbe.entity.User;
import com.ionidea.mothramxbe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authManager;

    private final JwtUtil jwtUtil;

    private final UserRepository userRepository;

    public AuthResponseDTO login(AuthRequest request) {

        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + request.getEmail()));

        String token = jwtUtil.generateToken(user.getEmail());

        String expiresAt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault())
                .format(Instant.ofEpochMilli(System.currentTimeMillis() + JwtUtil.EXPIRATION_MS));

        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        return AuthResponseDTO.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresAt(expiresAt)
                .expiresInSec(JwtUtil.EXPIRATION_MS / 1000)
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .roles(roles)
                .build();
    }

}

