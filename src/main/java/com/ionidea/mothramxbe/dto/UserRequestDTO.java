package com.ionidea.mothramxbe.dto;

import lombok.Data;
import java.util.Set;

@Data
public class UserRequestDTO {

    private String name;
    private String email;
    private String password;
    private Set<Long> roleIds;
    private Long leadId;
}