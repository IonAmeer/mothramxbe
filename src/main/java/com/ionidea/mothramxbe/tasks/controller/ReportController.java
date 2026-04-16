package com.ionidea.mothramxbe.tasks.controller;

import com.ionidea.mothramxbe.security.model.User;
import com.ionidea.mothramxbe.security.repository.UserRepository;
import com.ionidea.mothramxbe.tasks.dto.ReportDTO;
import com.ionidea.mothramxbe.tasks.model.Report;
import com.ionidea.mothramxbe.tasks.service.ExcelExportService;
import com.ionidea.mothramxbe.tasks.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin("*")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ExcelExportService excelService;

    @PreAuthorize("hasAuthority('TASK_CREATE')")
    @PostMapping
    public Report save(@RequestBody ReportDTO dto) {
        return reportService.save(dto);
    }

    @PreAuthorize("hasAuthority('AUTH_ADMIN')")
    @GetMapping("/admin")
    public List<Report> getAllForAdmin() {
        return reportService.getAllReports();
    }
//
//    @PreAuthorize("hasAuthority('TEAM_TASKS_READ')")
//    public List<Report> getReportsForLead(Authentication auth, @RequestParam Long monthId) {
//        String email = auth.getName();
//        User lead = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Lead not found"));
//        return reportService.getReportsForLead(lead.getId(), monthId);
//    }

//    @PreAuthorize("hasAuthority('TEAM_TASKS_READ')")
//    @GetMapping("/{id}")
//    public Report getById(@PathVariable Long id, Authentication auth) {
//        String email = auth.getName();
//        User lead = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Lead not found"));
//        return reportService.getReportByIdForLead(id, lead.getId());
//    }

//    @PreAuthorize("hasAuthority('TEAM_TASKS_READ')")
//    @GetMapping("/export/lead/{leadId}/developer/{developerId}")
//    public ResponseEntity<byte[]> exportDeveloperReports(@PathVariable Long leadId, @PathVariable Long developerId) {
//        List<Report> reports = reportService.getReportsByDeveloper(developerId, leadId);
//        ByteArrayInputStream stream = excelService.exportReports(reports);
//        return ResponseEntity
//                .ok()
//                .header("Content-Disposition", "attachment; filename=developer_reports.xlsx")
//                .body(stream.readAllBytes());
//    }

//    @PreAuthorize("hasAuthority('TASK_CREATE')")
//    @PutMapping("/status/{id}")
//    public Report updateStatus(
//            @PathVariable Long id,
//            @RequestParam String status,
//            @RequestParam(required = false) String reason,
//            Authentication auth) {
//        String email = auth.getName();
//        return reportService.updateStatus(id, status, email, "LEAD", reason);
//    }

//    @GetMapping("/developer/{developerId}")
//    public List<Report> getReportsByDeveloper(
//            @PathVariable Long developerId,
//            @RequestParam Long monthId,
//            Authentication auth) {
//        String email = auth.getName();
//        User lead = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Lead not found"));
//
//        return reportService.getReportsByDeveloperAndMonth(
//                developerId,
//                lead.getId(),
//                monthId
//        );
//    }
//
//    @GetMapping("/export/lead/{leadId}")
//    public ResponseEntity<byte[]> exportAllReports(@PathVariable Long leadId) {
//        List<Report> reports = reportService.getAllReportsByLead(leadId);
//        ByteArrayInputStream stream = excelService.exportReports(reports);
//        return ResponseEntity.ok()
//                .header("Content-Disposition", "attachment; filename=all_reports.xlsx")
//                .body(stream.readAllBytes());
//    }

}