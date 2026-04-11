package com.ionidea.mothramxbe.tasks.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ionidea.mothramxbe.security.model.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "jira_entries")
public class JiraEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ticketId;

    private String description;

    private Integer storyPoints;

    private Integer daysSpent;

    private Integer remaining;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id")
    @JsonIgnoreProperties({"jiraEntries", "user"})
    private Report report;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}