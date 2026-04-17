package com.ionidea.mothramxbe.tasks.service;

import com.ionidea.mothramxbe.security.model.User;
import com.ionidea.mothramxbe.security.repository.UserRepository;
import com.ionidea.mothramxbe.system.entity.RefMonth;
import com.ionidea.mothramxbe.system.repository.RefMonthRepository;
import com.ionidea.mothramxbe.tasks.dto.ReportDTO;
import com.ionidea.mothramxbe.tasks.model.Report;
import com.ionidea.mothramxbe.tasks.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {

    @Autowired
    private RefMonthRepository refMonthRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ReportRepository reportRepo;

    public List<Report> getReportsByDeveloper(Long developerId, Long leadId) {

        if (developerId == null || leadId == null) {
            throw new RuntimeException("DeveloperId and LeadId are required");
        }

        List<Report> reports = reportRepo.findByUserIdAndUser_Lead_Id(developerId, leadId);

        if (reports.isEmpty()) {
            throw new RuntimeException("No reports found for this developer under this lead");
        }

        return reports;
    }

    public Report save(ReportDTO dto) {

        Report r = new Report();

        // ✅ SET USER (you were missing this earlier)
        User user = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        r.setUser(user);

        // ✅ SET STATUS
        r.setStatus(dto.getStatus());

        // ✅ SET MONTH
        RefMonth rm = refMonthRepo.findById(dto.getRefMonthId())
                .orElseThrow(() -> new RuntimeException("Month not found"));
        r.setRefMonth(rm);

        return reportRepo.save(r);
    }

    public List<Report> getAllReports() {
        return reportRepo.findAll();
    }

    public List<Report> getReportsForLead(Long leadId, Long monthId) {

        if (leadId == null || monthId == null) {
            throw new RuntimeException("LeadId and MonthId are required");
        }

        return reportRepo.findByUser_Lead_IdAndRefMonthId(leadId, monthId);
    }

    public Report updateStatus(Long id, String status, String approvedBy, String role, String reason) {

        Report report = reportRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        if (!"LEAD".equalsIgnoreCase(role)) {
            throw new RuntimeException("Only Lead can approve/reject reports");
        }

        if (!"PENDING".equalsIgnoreCase(report.getStatus())) {
            throw new RuntimeException("Report already processed");
        }

        if (!"SUBMITTED".equalsIgnoreCase(report.getStatus())) {
            throw new RuntimeException("Report already processed");
        }

        if (!"APPROVED".equalsIgnoreCase(status) && !"REJECTED".equalsIgnoreCase(status)) {
            throw new RuntimeException("Invalid status value");
        }

        report.setStatus(status);
        report.setApprovedBy(approvedBy);

        if ("REJECTED".equalsIgnoreCase(status)) {
            if (reason == null || reason.isEmpty()) {
                throw new RuntimeException("Rejection reason is required");
            }
            report.setRejectionReason(reason);
        }

        return reportRepo.save(report);
    }

    public Report getReportByIdForLead(Long reportId, Long leadId) {

        Report report = reportRepo.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        if (!report.getUser().getLead().getId().equals(leadId)) {
            throw new RuntimeException("Access denied");
        }

        return report;
    }

    public List<Report> getReportsByDeveloperAndMonth(
            Long developerId,
            Long leadId,
            Long monthId) {

        return reportRepo.findByUserIdAndUser_Lead_IdAndRefMonthId(
                developerId,
                leadId,
                monthId
        );
    }

    public List<Report> getAllReportsByLead(Long leadId) {
        return reportRepo.findByUser_Lead_Id(leadId);
    }

}