package com.ionidea.mothramxbe.security.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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

    private Set<Long> authorityIds;

    private List<AuthorityDto> authorities;

}