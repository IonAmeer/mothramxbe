package com.ionidea.mothramxbe.security.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AuthResponseDTO {

    private String token;

    private String tokenType;

    private String expiresAt;

    private long expiresInSec;

    private Long userId;

    private String name;

    private String email;

    private List<String> roles;

}

