package com.ionidea.mothramxbe.tasks.dto;

import lombok.Data;

@Data
public class JiraEntryDTO {

    private Long id;

    private String ticketId;

    private String description;

    private Integer storyPoints;

    private Integer daysSpent;

    private Integer remaining;

    private Long reportId;

}
