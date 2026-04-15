package com.ionidea.mothramxbe.teamtasks.controller;

import com.ionidea.mothramxbe.security.model.User;
import com.ionidea.mothramxbe.security.repository.UserRepository;
import com.ionidea.mothramxbe.tasks.dto.ReportResponseDTO;
import com.ionidea.mothramxbe.tasks.model.Report;
import com.ionidea.mothramxbe.tasks.service.ExcelExportService;
import com.ionidea.mothramxbe.tasks.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lead")
// FIX 1: Remove @CrossOrigin("*") — CORS is already handled globally in SecurityConfig
//         (SecurityConfig allows http://localhost:4200 with credentials; mixing @CrossOrigin("*")
//          with allowCredentials=true causes a browser CORS error)
@PreAuthorize("hasRole('LEAD')")
// NOTE: @PreAuthorize("hasAuthority('LEAD')") is correct — CustomUserDetailsService
//       adds role authorities by name (e.g. "LEAD"), not "ROLE_LEAD".
//       The frontend RoleGuard uses 'lead' (lowercase) which must match the role name
//       stored in the DB. Make sure your DB role name is lowercase 'lead' OR
//       change the route guard to use 'LEAD' to match.
public class TeamTaskController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ExcelExportService excelService;

    // ================= REPORT =================

    // FIX 2: Return List<ReportResponseDTO> instead of List<Report>
    //         The raw Report entity has wrong field names for the frontend:
    //         id→reportId, user.name→developerName, jiraEntries→tasks, etc.
    @GetMapping("/reports")
    public List<ReportResponseDTO> getReportsForLead(Authentication auth,
                                                     @RequestParam Integer monthId) {
        String email = auth.getName();
        User lead = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Lead not found"));

        List<Report> reports = reportService.getReportsForLead(lead.getId().intValue(), monthId);

        return reports.stream()
                .map(ReportResponseDTO::from)
                .collect(Collectors.toList());
    }

    // FIX 3: Return ReportResponseDTO instead of raw Report entity
    @PutMapping("/reports/status/{id}")
    public ReportResponseDTO updateStatus(@PathVariable Integer id,
                                          @RequestParam String status,
                                          @RequestParam(required = false) String reason,
                                          Authentication auth) {

        // FIX 4: Frontend sends "Approved"/"Rejected" but ReportService checks for
        //        "APPROVED"/"REJECTED" and current status "PENDING".
        //        Frontend also shows status as "Submitted" but backend stores "PENDING".
        //        Normalise here so both sides work without changing the DB.
        String normalisedStatus = status.toUpperCase(); // "Approved" → "APPROVED"

        Report report = reportService.updateStatus(id, normalisedStatus, auth.getName(), "LEAD", reason);
        return ReportResponseDTO.from(report);
    }

    // ================= EXPORT =================

    @GetMapping("/export/developer/{developerId}")
    public ResponseEntity<byte[]> exportDeveloperReports(
            @PathVariable Integer developerId,
            Authentication auth) throws IOException {

        String email = auth.getName();
        User lead = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Lead not found"));

        List<Report> reports =
                reportService.getReportsByDeveloper(developerId, lead.getId().intValue());

        ByteArrayInputStream stream = excelService.exportReports(reports);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=developer_reports.xlsx")
                .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .body(stream.readAllBytes());
    }

    @GetMapping("/export/all")
    public ResponseEntity<byte[]> exportAllReports(Authentication auth) throws IOException {

        String email = auth.getName();
        User lead = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Lead not found"));

        List<Report> reports =
                reportService.getAllReportsByLead(lead.getId().intValue());

        ByteArrayInputStream stream = excelService.exportReports(reports);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=all_reports.xlsx")
                .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .body(stream.readAllBytes());
    }

}