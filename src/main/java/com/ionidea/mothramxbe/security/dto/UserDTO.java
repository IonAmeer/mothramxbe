package com.ionidea.mothramxbe.security.dto;

import com.ionidea.mothramxbe.security.model.Role;
import lombok.Data;

import java.util.Set;

@Data
public class UserDTO {

    private Long id;          // ✅ needed for update
    private String name;
    private String email;
    private String password;  // optional (for create)
    private Set<Long> roleIds;
    private Set<Role> roles;
    private Long leadId;
    private String leadName;
}
