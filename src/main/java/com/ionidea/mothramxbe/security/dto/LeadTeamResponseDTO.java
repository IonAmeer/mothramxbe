package com.ionidea.mothramxbe.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LeadTeamResponseDTO {

    private Long leadId;
    private String leadName;

    private List<DeveloperDTO> developers;

    @Data
    @AllArgsConstructor
    public static class DeveloperDTO {
        private Long id;
        private String name;
        private String email;
    }
}