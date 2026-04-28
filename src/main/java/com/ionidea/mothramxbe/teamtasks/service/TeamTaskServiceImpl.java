package com.ionidea.mothramxbe.teamtasks.service;

import com.ionidea.mothramxbe.tasks.model.Report;
import com.ionidea.mothramxbe.tasks.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamTaskServiceImpl {

    @Autowired
    private ReportRepository reportRepository;

    public List<Report> getReportsForLead(Long leadId, Long monthId) {

        if (leadId == null || monthId == null) {
            throw new RuntimeException("LeadId and MonthId are required");
        }
        return reportRepository.findReportByLead(leadId, monthId);
    }

    public Report updateStatus(Long id, String status, String approvedBy, String role, String reason) {

        Report report = null;
//                reportRepo.findById(id).orElseThrow(() -> new RuntimeException("Report not found"));

        if (!"LEAD".equalsIgnoreCase(role)) {
            throw new RuntimeException("Only Lead can approve/reject reports");
        }

        if (!"PENDING".equalsIgnoreCase(report.getLeadStatus())) {
            throw new RuntimeException("Report already processed");
        }

        if (!"SUBMITTED".equalsIgnoreCase(report.getLeadStatus())) {
            throw new RuntimeException("Report already processed");
        }

        if (!"APPROVED".equalsIgnoreCase(status) && !"REJECTED".equalsIgnoreCase(status)) {
            throw new RuntimeException("Invalid status value");
        }

        report.setLeadStatus(status);

        if ("REJECTED".equalsIgnoreCase(status)) {
            if (reason == null || reason.isEmpty()) {
                throw new RuntimeException("Rejection reason is required");
            }
            report.setLeadStatus(status);
        }

        return null;
//                reportRepo.save(report);
    }

}