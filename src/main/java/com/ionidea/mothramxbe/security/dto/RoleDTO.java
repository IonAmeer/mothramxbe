package com.ionidea.mothramxbe.security.dto;

import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {

    private Long id;

    private String name;

    private Set<Long> authorityIds;   // ✅ null = don't update

    private List<AuthorityDto> authorities; // ✅ response only
}