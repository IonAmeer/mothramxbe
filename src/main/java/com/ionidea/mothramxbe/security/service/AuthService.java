package com.ionidea.mothramxbe.security.service;

import com.ionidea.mothramxbe.security.dto.AuthRequestDto;
import com.ionidea.mothramxbe.security.dto.AuthResponseDto;
import com.ionidea.mothramxbe.security.model.User;
import com.ionidea.mothramxbe.security.repository.UserRepository;
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

    public AuthResponseDto login(AuthRequestDto request) {

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

        List<String> roles = user.getUserRoles().stream()
                .map(ur -> ur.getRole().getName())
                .collect(Collectors.toList());

        return new AuthResponseDto()
                .setToken(token)
                .setTokenType("Bearer")
                .setExpiresAt(expiresAt)
                .setExpiresInSec(JwtUtil.EXPIRATION_MS / 1000)
                .setUserId(user.getId())
                .setName(user.getName())
                .setEmail(user.getEmail())
                .setRoles(roles);
    }

}

