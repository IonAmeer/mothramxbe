package com.ionidea.mothramxbe.tasks.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class JiraEntryDTO {

    private Long id;

    private String ticketId;

    private String description;

    private Integer storyPoints;

    private Integer daysSpent;

    private Integer remaining;

    private Long reportId;

}