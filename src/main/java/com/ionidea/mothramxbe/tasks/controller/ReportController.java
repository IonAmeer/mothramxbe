package com.ionidea.mothramxbe.tasks.controller;

import com.ionidea.mothramxbe.security.model.User;
import com.ionidea.mothramxbe.security.repository.UserRepository;
import com.ionidea.mothramxbe.tasks.model.Report;
import com.ionidea.mothramxbe.tasks.service.ExcelExportService;
import com.ionidea.mothramxbe.tasks.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public Report save(@RequestBody Report report) {
        return reportService.saveReport(report);
    }

    @GetMapping("/admin")
    public List<Report> getAllForAdmin() {
        return reportService.getAllReports();
    }

    @GetMapping("/lead")
    public List<Report> getReportsForLead(Authentication auth, @RequestParam Integer monthId) {
        String email = auth.getName();
        User lead = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Lead not found"));
        return reportService.getReportsForLead(lead.getId().intValue(), monthId);
    }

    @GetMapping("/{id}")
    public Report getById(@PathVariable Integer id, Authentication auth) {
        String email = auth.getName();
        User lead = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Lead not found"));
        return reportService.getReportByIdForLead(id, lead.getId().intValue());
    }

    @GetMapping("/export/lead/{leadId}/developer/{developerId}")
    public ResponseEntity<byte[]> exportDeveloperReports(@PathVariable Integer leadId, @PathVariable Integer developerId) {
        List<Report> reports = reportService.getReportsByDeveloper(developerId, leadId);
        ByteArrayInputStream stream = excelService.exportReports(reports);
        return ResponseEntity
                .ok()
                .header("Content-Disposition", "attachment; filename=developer_reports.xlsx")
                .body(stream.readAllBytes());
    }

    @PutMapping("/status/{id}")
    public Report updateStatus(
            @PathVariable Integer id,
            @RequestParam String status,
            @RequestParam(required = false) String reason,
            Authentication auth) {
        String email = auth.getName();
        return reportService.updateStatus(id, status, email, "LEAD", reason);
    }

    @GetMapping("/developer/{developerId}")
    public List<Report> getReportsByDeveloper(
            @PathVariable Integer developerId,
            @RequestParam Integer monthId,
            Authentication auth) {
        String email = auth.getName();
        User lead = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Lead not found"));

        return reportService.getReportsByDeveloperAndMonth(
                developerId,
                lead.getId().intValue(),
                monthId
        );
    }

    @GetMapping("/export/lead/{leadId}")
    public ResponseEntity<byte[]> exportAllReports(@PathVariable Integer leadId) {
        List<Report> reports = reportService.getAllReportsByLead(leadId);
        ByteArrayInputStream stream = excelService.exportReports(reports);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=all_reports.xlsx")
                .body(stream.readAllBytes());
    }

}