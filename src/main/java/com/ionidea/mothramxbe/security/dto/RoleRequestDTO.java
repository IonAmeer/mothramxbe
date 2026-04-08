package com.ionidea.mothramxbe.security.dto;

import lombok.Data;

import java.util.Set;

@Data
public class RoleRequestDTO {

    private String name;

    private Set<Long> authorityIds; // 👈 important

}