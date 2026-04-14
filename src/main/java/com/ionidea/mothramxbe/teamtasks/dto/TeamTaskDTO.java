package com.ionidea.mothramxbe.teamtasks.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamTaskDTO {

    private Long reportId;

    private String developerName;

    private String status;

    private Long monthId;

    private String approvedBy;

}