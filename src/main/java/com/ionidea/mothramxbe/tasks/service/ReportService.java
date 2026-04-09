package com.ionidea.mothramxbe.tasks.service;

import com.ionidea.mothramxbe.security.model.User;
import com.ionidea.mothramxbe.security.repository.UserRepository;
import com.ionidea.mothramxbe.tasks.model.RefMonth;
import com.ionidea.mothramxbe.tasks.model.Report;
import com.ionidea.mothramxbe.tasks.repository.RefMonthRepository;
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

    // ✅ GET REPORTS BY DEVELOPER UNDER A LEAD
    public List<Report> getReportsByDeveloper(Integer developerId, Integer leadId) {

        if (developerId == null || leadId == null) {
            throw new RuntimeException("DeveloperId and LeadId are required");
        }

        List<Report> reports = reportRepo.findByUserIdAndUser_Lead_Id(developerId, leadId);

        if (reports.isEmpty()) {
            throw new RuntimeException("No reports found for this developer under this lead");
        }

        return reports;
    }

    // ✅ SAVE REPORT
    public Report saveReport(Report report) {

        // ✅ FETCH USER FROM DB (IMPORTANT FIX)
        if (report.getUser() != null && report.getUser().getId() != null) {
            User user = userRepo.findById(report.getUser().getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            report.setUser(user);
        }

        // ✅ HANDLE MONTH
        if (report.getRefMonthId() != null) {
            RefMonth rm = refMonthRepo
                    .findById(report.getRefMonthId())
                    .orElse(null);

            report.setRefMonth(rm);
        }

        // ✅ DEFAULT STATUS
        if (report.getStatus() == null) {
            report.setStatus("PENDING");
        }

        return reportRepo.save(report);
    }

    public List<Report> getAllReports() {
        return reportRepo.findAll();
    }

    // ✅ GET REPORT BY ID
    public Report getReportById(Integer id) {
        return reportRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));
    }

    // 🔥 MAIN LEAD API (MOST IMPORTANT)
    public List<Report> getReportsForLead(Integer leadId, Integer monthId) {

        if (leadId == null || monthId == null) {
            throw new RuntimeException("LeadId and MonthId are required");
        }

        return reportRepo.findByUser_Lead_IdAndRefMonthId(leadId, monthId);
    }

    // ✅ APPROVE / REJECT REPORT
    public Report updateStatus(Integer id, String status, String approvedBy, String role, String reason) {

        Report report = reportRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        // ❗ Only LEAD allowed
        if (!"LEAD".equalsIgnoreCase(role)) {
            throw new RuntimeException("Only Lead can approve/reject reports");
        }

        // ❗ Only if still pending
        if (!"PENDING".equalsIgnoreCase(report.getStatus())) {
            throw new RuntimeException("Report already processed");
        }

        // ❗ Validate status
        if (!"APPROVED".equalsIgnoreCase(status) && !"REJECTED".equalsIgnoreCase(status)) {
            throw new RuntimeException("Invalid status value");
        }

        report.setStatus(status);
        report.setApprovedBy(approvedBy);

        // If rejected → reason is mandatory
        if ("REJECTED".equalsIgnoreCase(status)) {
            if (reason == null || reason.isEmpty()) {
                throw new RuntimeException("Rejection reason is required");
            }
            report.setRejectionReason(reason);
        }

        return reportRepo.save(report);
    }

    public Report getReportByIdForLead(Integer reportId, Integer leadId) {

        Report report = reportRepo.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

//        if (!report.getUser().getLeadId().equals(leadId)) {
//            throw new RuntimeException("Access denied");
//        }

        return report;
    }

    public List<Report> getReportsByDeveloperAndMonth(
            Integer developerId,
            Integer leadId,
            Integer monthId) {

        return reportRepo.findByUserIdAndUser_Lead_IdAndRefMonthId(
                developerId,
                leadId,
                monthId
        );
    }

    public List<Report> getAllReportsByLead(Integer leadId) {
        return reportRepo.findByUser_Lead_Id(leadId);
    }

}