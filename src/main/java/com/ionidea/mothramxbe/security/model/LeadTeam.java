package com.ionidea.mothramxbe.security.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lead_team")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeadTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lead_id", nullable = false)
    @JsonIgnoreProperties({"password", "userRoles"})
    private User lead;

    @OneToOne
    @JoinColumn(name = "developer_id", nullable = false, unique = true)
    @JsonIgnoreProperties({"password", "userRoles"})
    private User developer;
}