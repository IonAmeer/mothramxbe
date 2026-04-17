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
    public Report save(@RequestBody ReportDTO dto, Authentication auth) {
        String email = auth.getName();
        return reportService.save(dto, email);
    }

    @GetMapping("/current")
    public Report getOrCreateCurrentReport(Authentication auth) {

        String email = auth.getName();

        return reportService.getOrCreateCurrentReport(email);
    }

}