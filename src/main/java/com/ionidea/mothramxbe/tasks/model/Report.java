package com.ionidea.mothramxbe.tasks.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ionidea.mothramxbe.security.model.User;
import com.ionidea.mothramxbe.system.entity.RefMonth;
import jakarta.persistence.*;
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

    // ✅ USER
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"password", "userRoles", "developers", "lead"})
    private User user;

    private String status;

    private String approvedBy;

    private String rejectionReason;

    @Column(name = "ref_month_id")
    private Long refMonthId;

    // ✅ OBJECT MAPPING
    @ManyToOne
    @JoinColumn(name = "ref_month_id", insertable = false, updatable = false)
    private RefMonth refMonth;

    // ✅ CHILD TABLES (HIDE FROM RESPONSE)
    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL)
    @JsonIgnore   // 🔥 IMPORTANT (avoid huge JSON)
    private List<JiraEntry> jiraEntries;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL)
    @JsonIgnore   // 🔥 IMPORTANT
    private List<LeaveEntry> leaveEntries;
}