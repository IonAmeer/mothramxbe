package com.ionidea.mothramxbe.tasks.model;

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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String status;

    private String approvedBy;

    @Column(name = "ref_month_id")
    private Long refMonthId;

    private String rejectionReason;

    @ManyToOne
    @JoinColumn(name = "ref_month_id", insertable = false, updatable = false)
    private RefMonth refMonth;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL)
    private List<JiraEntry> jiraEntries;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL)
    private List<LeaveEntry> leaveEntries;

}
