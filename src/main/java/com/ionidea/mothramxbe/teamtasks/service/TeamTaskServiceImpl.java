package com.ionidea.mothramxbe.teamtasks.service;

import com.ionidea.mothramxbe.tasks.dto.JiraEntryDTO;
import com.ionidea.mothramxbe.tasks.dto.LeaveEntryDTO;
import com.ionidea.mothramxbe.tasks.model.JiraEntry;
import com.ionidea.mothramxbe.tasks.model.LeaveEntry;
import com.ionidea.mothramxbe.tasks.model.Report;
import com.ionidea.mothramxbe.tasks.repository.ReportRepository;
import com.ionidea.mothramxbe.tasks.dto.ReportDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamTaskServiceImpl {

    @Autowired
    private ReportRepository reportRepository;

    public List<ReportDTO> getReportsForLead(Long leadId, Long monthId) {

        if (leadId == null || monthId == null) {
            throw new RuntimeException("LeadId and MonthId are required");
        }

        List<Report> reports = reportRepository.findReportByLead(leadId, monthId);

        return reports.stream()
                .map(this::mapToDTO)
                .toList();
    }

    public ReportDTO updateStatus(Long id, String status) {

        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        if (report.getLeadStatus() != null &&
                (report.getLeadStatus().equalsIgnoreCase("APPROVED")
                        || report.getLeadStatus().equalsIgnoreCase("REJECTED"))) {
            throw new RuntimeException("Report already processed");
        }

        if (!"APPROVED".equalsIgnoreCase(status) &&
                !"REJECTED".equalsIgnoreCase(status)) {
            throw new RuntimeException("Invalid status value");
        }

        report.setLeadStatus(status.toUpperCase());

        Report saved = reportRepository.save(report);

        return mapToDTO(saved);
    }

    // ================= MAPPER =================
    private ReportDTO mapToDTO(Report report) {

        ReportDTO dto = new ReportDTO();

        dto.setId(report.getId());

        if (report.getUser() != null) {
            dto.setUserId(report.getUser().getId());
            dto.setDeveloperId(report.getUser().getId());
            dto.setDeveloperName(report.getUser().getName());
        } else {
            dto.setDeveloperName("Unknown");
        }

        dto.setStatus(normalizeStatus(report.getLeadStatus()));
        dto.setRefMonthId(report.getRefMonth().getId());

        // -------- JIRA --------
        List<JiraEntryDTO> jiraList = report.getJiraEntries() == null
                ? List.of()
                : report.getJiraEntries().stream()
                .map(this::mapJiraToDTO)
                .toList();

        dto.setJiraEntries(jiraList);

        // -------- LEAVES --------
        List<LeaveEntryDTO> leaveList = report.getLeaveEntries() == null
                ? List.of()
                : report.getLeaveEntries().stream()
                .map(this::mapLeaveToDTO)
                .toList();

        dto.setLeaveEntries(leaveList);

        // -------- COMPUTATION --------
        int totalStoryPoints = jiraList.stream()
                .mapToInt(j -> j.getStoryPoints() == null ? 0 : j.getStoryPoints())
                .sum();

        int totalSpent = jiraList.stream()
                .mapToInt(j -> j.getDaysSpent() == null ? 0 : j.getDaysSpent())
                .sum();

        dto.setEffectiveWorkingDays(totalStoryPoints);
        dto.setLoggedWorkingDays(totalSpent);

        // Optional (if needed)
        dto.setTotalWorkingDays(totalStoryPoints);

        return dto;
    }

    private String normalizeStatus(String status) {

        if (status == null) return "Unknown";

        return switch (status.toUpperCase()) {
            case "PENDING", "SUBMITTED" -> "Submitted";
            case "APPROVED" -> "Approved";
            case "REJECTED" -> "Rejected";
            case "DRAFT" -> "Draft";
            default -> status;
        };
    }

    private JiraEntryDTO mapJiraToDTO(JiraEntry jira) {

        JiraEntryDTO dto = new JiraEntryDTO();

        dto.setId(jira.getId());
        dto.setTicketId(jira.getTicketId());
        dto.setDescription(jira.getDescription());
        dto.setStoryPoints(jira.getStoryPoints());
        dto.setDaysSpent(jira.getDaysSpent());
        dto.setRemaining(jira.getRemaining());

        if (jira.getReport() != null) {
            dto.setReportId(jira.getReport().getId());
        }

        return dto;
    }

    private LeaveEntryDTO mapLeaveToDTO(LeaveEntry leave) {

        LeaveEntryDTO dto = new LeaveEntryDTO();

        dto.setId(leave.getId());
        dto.setDate(leave.getDate());
        dto.setDuration(leave.getDuration());
        dto.setReason(leave.getReason());

        // LeaveType mapping
        if (leave.getLeaveType() != null) {
            dto.setLeaveTypeId(leave.getLeaveType().getId());
        }

        // Report mapping
        if (leave.getReport() != null) {
            dto.setReportId(leave.getReport().getId());
        }

        return dto;
    }

    public List<Report> getAllReportsEntityByLeadAndMonth(Long leadId, Long monthId) {
        return reportRepository.findByLeadAndMonth(leadId, monthId);
    }

    public List<Report> getReportsEntityByDeveloperAndMonth(Long developerId, Long leadId, Long monthId
    ) {
        return reportRepository.findByDeveloperLeadAndMonth(developerId, leadId, monthId);
    }

}