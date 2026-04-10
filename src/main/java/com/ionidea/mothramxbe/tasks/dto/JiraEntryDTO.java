package com.ionidea.mothramxbe.tasks.dto;

import lombok.Data;

@Data
public class JiraEntryDTO {

    private Integer id;

    private String ticketId;

    private String description;

    private Integer storyPoints;

    private Integer daysSpent;

    private Integer remaining;

    private Integer reportId;

}
