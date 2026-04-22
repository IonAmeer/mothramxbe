package com.ionidea.mothramxbe.teamtasks.controller;

import com.ionidea.mothramxbe.security.model.User;
import com.ionidea.mothramxbe.security.repository.UserRepository;
import com.ionidea.mothramxbe.tasks.dto.ReportDTO;
import com.ionidea.mothramxbe.tasks.model.Report;
import com.ionidea.mothramxbe.tasks.service.ExcelExportService;
import com.ionidea.mothramxbe.teamtasks.service.TeamTaskServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@RequestMapping("/team-tasks")
public class TeamTaskController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private TeamTaskServiceImpl teamTaskService;

    @Autowired
    private ExcelExportService excelService;

    @PreAuthorize("hasAuthority('TEAM_TASKS_READ')")
    @GetMapping("/reports")
    public List<Report> getReportsForLead(Authentication auth, @RequestParam Long refMonthId) {
        String email = auth.getName();
        User lead = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Lead not found"));
        return teamTaskService.getReportsForLead(lead.getId(), refMonthId);
    }

    @PreAuthorize("hasAuthority('TEAM_TASKS_UPDATE')")
    @PutMapping("/reports/status/{id}")
    public Report updateStatus(@PathVariable Long id,
                               @RequestParam String status,
                               @RequestParam(required = false) String reason,
                               Authentication auth) {
        return teamTaskService.updateStatus(id, status, auth.getName(), "LEAD", reason);
    }

    // Put this in separate package
    @PreAuthorize("hasAuthority('TEAM_TASKS_READ')")
    @GetMapping("/export/developer/{developerId}")
    public ResponseEntity<byte[]> exportDeveloperReports(
            @PathVariable Integer developerId,
            Authentication auth) {

        String email = auth.getName();
        User lead = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Lead not found"));

        List<Report> reports = null;
//                reportService.getReportsByDeveloper(developerId.longValue(), lead.getId().longValue());

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

        List<Report> reports = null;
//                reportService.getAllReportsByLead(lead.getId());

        ByteArrayInputStream stream = excelService.exportReports(reports);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=all_reports.xlsx")
                .body(stream.readAllBytes());
    }

}