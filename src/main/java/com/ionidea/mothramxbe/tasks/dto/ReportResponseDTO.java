package com.ionidea.mothramxbe.tasks.dto;

import com.ionidea.mothramxbe.tasks.model.JiraEntry;
import com.ionidea.mothramxbe.tasks.model.LeaveEntry;
import com.ionidea.mothramxbe.tasks.model.Report;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO returned by /api/lead/reports
 * Field names match exactly what the Angular LeadPageComponent expects.
 */
@Data
public class ReportResponseDTO {

    private Integer reportId;           // ← frontend: r.reportId

    private String developerName;       // ← frontend: r.developerName

    private String status;              // ← frontend: r.status

    private Integer monthId;            // ← frontend: r.monthId

    private String approvedBy;          // ← frontend: r.approvedBy

    // sp / taken / wd are aggregated from jiraEntries — Report has no direct column for them
    private int sp;                     // ← frontend: r.sp   (sum of storyPoints)

    private int taken;                  // ← frontend: r.taken (sum of daysSpent)

    private int wd;                     // ← frontend: r.wd   (placeholder — set to 0 until you add the column)

    private List<TaskDTO> tasks;        // ← frontend: r.tasks

    private List<LeaveDTO> leaves;      // ← frontend: r.leaves

    // ----------------------------------------------------------------
    // Nested DTOs — field names match what the HTML template uses
    // ----------------------------------------------------------------

    public static ReportResponseDTO from(Report report) {

        ReportResponseDTO dto = new ReportResponseDTO();

        dto.setReportId(report.getId().intValue());
        dto.setDeveloperName(report.getUser() != null ? report.getUser().getName() : "");
        dto.setStatus(report.getStatus());
        dto.setMonthId(report.getRefMonthId().intValue());
        dto.setApprovedBy(report.getApprovedBy());

        // ---- tasks ----
        List<JiraEntry> jiraEntries = report.getJiraEntries() != null
                ? report.getJiraEntries()
                : Collections.emptyList();

        dto.setTasks(jiraEntries.stream().map(j -> {
            TaskDTO t = new TaskDTO();
            t.setJiraTicket(j.getTicketId());
            t.setTitle(j.getDescription());
            t.setSp(j.getStoryPoints());
            t.setTaken(j.getDaysSpent());
            t.setRemaining(j.getRemaining());
            return t;
        }).collect(Collectors.toList()));

        // ---- leaves ----
        List<LeaveEntry> leaveEntries = report.getLeaveEntries() != null
                ? report.getLeaveEntries()
                : Collections.emptyList();

        dto.setLeaves(leaveEntries.stream().map(l -> {
            LeaveDTO lv = new LeaveDTO();
            lv.setDate(l.getDate());
            lv.setType(l.getLeaveType() != null ? l.getLeaveType().getName() : "");
            lv.setDuration(l.getDuration());
            lv.setReason(l.getReason());
            return lv;
        }).collect(Collectors.toList()));

        // ---- aggregated totals ----
        dto.setSp(jiraEntries.stream()
                .mapToInt(j -> j.getStoryPoints() != null ? j.getStoryPoints() : 0).sum());
        dto.setTaken(jiraEntries.stream()
                .mapToInt(j -> j.getDaysSpent() != null ? j.getDaysSpent() : 0).sum());
        dto.setWd(0); // update this when you add a working-days column to Report

        return dto;
    }

    @Data
    public static class TaskDTO {

        private String jiraTicket;   // ← task.jiraTicket  (was: ticketId)

        private String title;        // ← task.title       (was: description)

        private Integer sp;          // ← task.sp          (was: storyPoints)

        private Integer taken;       // ← task.taken       (was: daysSpent)

        private Integer remaining;   // ← task.remaining   ✓ already matches

    }

    // ----------------------------------------------------------------
    // Static factory — converts Report entity → this DTO
    // ----------------------------------------------------------------

    @Data
    public static class LeaveDTO {

        private String date;         // ← leave.date       ✓ already matches

        private String type;         // ← leave.type       (was: leaveType.name)

        private String duration;     // ← leave.duration   ✓ already matches

        private String reason;       // ← leave.reason     ✓ already matches

    }

}