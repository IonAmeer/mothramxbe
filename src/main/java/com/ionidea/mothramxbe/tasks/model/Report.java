package com.ionidea.mothramxbe.tasks.model;

import com.ionidea.mothramxbe.security.model.User;
import com.ionidea.mothramxbe.system.entity.RefMonth;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String leadStatus;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "ref_month_id")
    private RefMonth refMonth;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<JiraEntry> jiraEntries;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<LeaveEntry> leaveEntries;

    /**
     * Total number of working days in the given month.
     * <p>
     * This is calculated as:
     * total days in month - weekends - company holidays.
     */
    private Integer totalWorkingDays;

    /**
     * Number of effective working days available for the user.
     * <p>
     * This is calculated as:
     * totalWorkingDays - total leave days taken by the user.
     */
    private Double effectiveWorkingDays;

    /**
     * Total number of days logged by the user in Jira.
     * <p>
     * This is calculated as:
     * sum of all daysSpent from associated Jira entries.
     */
    private Double loggedWorkingDays;

    /**
     * Indicates whether the user submitted leave entries
     * before logging any Jira work entries.
     * <p>
     * true  -> leave entries were submitted first
     * false -> Jira work entries were submitted first
     */
    private Boolean isLeaveSubmittedFirst;

}