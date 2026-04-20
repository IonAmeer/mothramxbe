package com.ionidea.mothramxbe.security.dto;

import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;

    private String name;

    private String email;

    private String password;

    private Set<Long> roleIds;

    private List<RoleDTO> roles;

}