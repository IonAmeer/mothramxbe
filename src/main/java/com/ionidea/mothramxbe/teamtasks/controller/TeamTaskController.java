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
    public List<ReportDTO> getReportsForLead(@RequestParam Long monthId,
                                             Authentication auth) {

        String email = auth.getName();

        User lead = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Lead not found"));

        return teamTaskService.getReportsForLead(lead.getId(), monthId);
    }

    @PreAuthorize("hasAuthority('TEAM_TASKS_UPDATE')")
    @PutMapping("/reports/status/{id}")
    public ReportDTO updateStatus(@PathVariable Long id, @RequestParam String status) {
        return teamTaskService.updateStatus(id, status);
    }

    // Put this in separate package
    @PreAuthorize("hasAuthority('TEAM_TASKS_READ')")
    @GetMapping("/export/developer/{developerId}")
    public ResponseEntity<byte[]> exportDeveloperReports(@PathVariable Long developerId, @RequestParam Long monthId, Authentication auth) {

        String email = auth.getName();

        User lead = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Lead not found"));

        List<Report> reports = teamTaskService.getReportsEntityByDeveloperAndMonth(developerId, lead.getId(), monthId);

        ByteArrayInputStream stream = excelService.exportReports(reports);

        return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=developer_reports.xlsx").body(stream.readAllBytes());
    }

    // ================= EXPORT ALL REPORTS =================

    @PreAuthorize("hasAuthority('TEAM_TASKS_READ')")
    @GetMapping("/export/all")
    public ResponseEntity<byte[]> exportAllReports(@RequestParam Long monthId, Authentication auth) {

        String email = auth.getName();

        User lead = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Lead not found"));

        List<Report> reports = teamTaskService.getAllReportsEntityByLeadAndMonth(lead.getId(), monthId);

        ByteArrayInputStream stream = excelService.exportReports(reports);

        return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=all_reports.xlsx").body(stream.readAllBytes());
    }

}