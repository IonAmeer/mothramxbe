package com.ionidea.mothramxbe.tasks.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReportDTO {

    // ---------------- BASIC FIELDS ----------------
    private Long id;

    private Long userId;

    private Long developerId;

    private String developerName;

    private String status;

    private String approvedBy;

    private String rejectionReason;

    private Long refMonthId;

    // ---------------- AGGREGATED FIELDS ----------------
    private Integer totalWorkingDays;

    private Integer effectiveWorkingDays;

    private Integer loggedWorkingDays;

    private List<JiraEntryDTO> jiraEntries;

    private List<LeaveEntryDTO> leaveEntries;

}