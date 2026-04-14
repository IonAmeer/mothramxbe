package com.ionidea.mothramxbe.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AuthResponseDto {

    private String token;

    private String tokenType;

    private String expiresAt;

    private long expiresInSec;

    private Long userId;

    private String name;

    private String email;

    private List<String> roles;

}

