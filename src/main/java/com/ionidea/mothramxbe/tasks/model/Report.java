package com.ionidea.mothramxbe.tasks.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ionidea.mothramxbe.security.model.User;
import com.ionidea.mothramxbe.system.entity.RefMonth;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"password", "userRoles", "developers", "lead"})
    private User user;

    private String status;

    private String approvedBy;

    private String rejectionReason;

    @Column(name = "ref_month_id")
    private Long refMonthId;

    @ManyToOne
    @JoinColumn(name = "ref_month_id", insertable = false, updatable = false)
    private RefMonth refMonth;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<JiraEntry> jiraEntries;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<LeaveEntry> leaveEntries;
}