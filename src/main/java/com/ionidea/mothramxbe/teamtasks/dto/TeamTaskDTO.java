package com.ionidea.mothramxbe.teamtasks.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamTaskDTO {

    private Integer reportId;

    private String developerName;

    private String status;

    private Integer monthId;

    private String approvedBy;

}