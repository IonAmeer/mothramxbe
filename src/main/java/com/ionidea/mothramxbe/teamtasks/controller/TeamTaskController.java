package com.ionidea.mothramxbe.teamtasks.controller;

import com.ionidea.mothramxbe.security.model.User;
import com.ionidea.mothramxbe.security.repository.UserRepository;
import com.ionidea.mothramxbe.tasks.model.JiraEntry;
import com.ionidea.mothramxbe.tasks.model.LeaveEntry;
import com.ionidea.mothramxbe.tasks.model.Report;
import com.ionidea.mothramxbe.tasks.service.ExcelExportService;
import com.ionidea.mothramxbe.tasks.service.JiraEntryService;
import com.ionidea.mothramxbe.tasks.service.LeaveEntryService;
import com.ionidea.mothramxbe.tasks.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@RequestMapping("/api/lead")
@CrossOrigin("*")

public class TeamTaskController {

    @Autowired
    private JiraEntryService jiraService;

    @Autowired
    private LeaveEntryService leaveService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ExcelExportService excelService;

    // ================= JIRA =================

    @PreAuthorize("hasAuthority('TEAM_TASKS_READ')")
    @GetMapping("/jira")
    public List<JiraEntry> getAllJira() {
        return jiraService.getAll();
    }

    // ================= LEAVE =================

    @PreAuthorize("hasAuthority('TEAM_TASKS_READ')")
    @GetMapping("/leave")
    public List<LeaveEntry> getAllLeaves() {
        return leaveService.getAll();
    }

    // ================= REPORT =================

    @PreAuthorize("hasAuthority('TEAM_TASKS_READ')")
    @GetMapping("/reports")
    public List<Report> getReportsForLead(Authentication auth,
                                          @RequestParam Long refMonthId) {

        String email = auth.getName();
        User lead = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Lead not found"));

        return reportService.getReportsForLead(lead.getId(), refMonthId);
    }

    @PreAuthorize("hasAuthority('TEAM_TASKS_UPDATE')")
    @PutMapping("/reports/status/{id}")
    public Report updateStatus(@PathVariable Long id,
                               @RequestParam String status,
                               @RequestParam(required = false) String reason,
                               Authentication auth) {

        return reportService.updateStatus(id, status, auth.getName(), "LEAD", reason);
    }

    @PreAuthorize("hasAuthority('TEAM_TASKS_READ')")
    @GetMapping("/reports/developer/{developerId}")
    public List<Report> getReportsByDeveloper(
            @PathVariable Long developerId,
            @RequestParam Long monthId,
            Authentication auth) {

        String email = auth.getName();
        User lead = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Lead not found"));

        return reportService.getReportsByDeveloperAndMonth(
                developerId,
                lead.getId(),
                monthId
        );
    }

    // ================= EXPORT =================

    @PreAuthorize("hasAuthority('TEAM_TASKS_READ')")
    @GetMapping("/export/developer/{developerId}")
    public ResponseEntity<byte[]> exportDeveloperReports(
            @PathVariable Integer developerId,
            Authentication auth) {

        String email = auth.getName();
        User lead = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Lead not found"));

        List<Report> reports =
                reportService.getReportsByDeveloper(developerId.longValue(), lead.getId().longValue());

        ByteArrayInputStream stream = excelService.exportReports(reports);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=developer_reports.xlsx")
                .body(stream.readAllBytes());
    }

    @PreAuthorize("hasAuthority('TEAM_TASKS_READ')")
    @GetMapping("/export/all")
    public ResponseEntity<byte[]> exportAllReports(Authentication auth) {

        String email = auth.getName();
        User lead = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Lead not found"));

        List<Report> reports =
                reportService.getAllReportsByLead(lead.getId());

        ByteArrayInputStream stream = excelService.exportReports(reports);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=all_reports.xlsx")
                .body(stream.readAllBytes());
    }

}