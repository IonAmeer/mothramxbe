package com.ionidea.mothramxbe.tasks.dto;

import com.ionidea.mothramxbe.tasks.model.JiraEntry;
import com.ionidea.mothramxbe.tasks.model.LeaveEntry;
import lombok.Data;

import java.util.List;

@Data
public class ReportResponseDTO {

    private Long id;

    private Long userId;

    private String userName;

    private Long refMonthId;

    private String month;

    private int year;

    private String status;

    // ✅ Updated naming (aligned with entity)
    private boolean isLeaveSubmittedFirst;

    // ✅ New fields from Report entity
    private Integer totalWorkingDays;

    private Double effectiveWorkingDays;

    private Double loggedWorkingDays;

    private List<JiraEntryDTO> jiraEntries;

    private List<LeaveEntryDTO> leaveEntries;

}