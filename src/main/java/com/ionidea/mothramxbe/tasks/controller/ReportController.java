package com.ionidea.mothramxbe.tasks.controller;

import com.ionidea.mothramxbe.tasks.dto.ReportResponseDTO;
import com.ionidea.mothramxbe.tasks.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/report")
@CrossOrigin("*")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PreAuthorize("hasAuthority('TASK_READ')")
    @GetMapping("/current")
    public ReportResponseDTO getCurrentReport(Authentication auth) {
        return reportService.getCurrentReportWithSummary(auth.getName());
    }

    @PreAuthorize("hasAuthority('TASK_CREATE')")
    @PostMapping("/submit")
    public ReportResponseDTO submitReport(Authentication auth) {
        return reportService.submitReport(auth.getName());
    }

    @PreAuthorize("hasAuthority('TASK_UPDATE')")
    @PostMapping("/lock-leaves")
    public String lockLeaves(Authentication auth) {
        reportService.lockLeaves(auth.getName());
        return "Leaves locked successfully";
    }

}