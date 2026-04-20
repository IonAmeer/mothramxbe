package com.ionidea.mothramxbe.tasks.service;

import com.ionidea.mothramxbe.security.model.User;
import com.ionidea.mothramxbe.security.model.User;
import com.ionidea.mothramxbe.security.repository.UserRepository;
import com.ionidea.mothramxbe.system.entity.RefMonth;
import com.ionidea.mothramxbe.system.repository.RefMonthRepository;
import com.ionidea.mothramxbe.tasks.dto.ReportDTO;
import com.ionidea.mothramxbe.tasks.model.Report;
import com.ionidea.mothramxbe.tasks.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

//        List<Report> reports = reportRepo.findByUserIdAndUser_Lead_Id(developerId, leadId);
        List<Report> reports = null;

        if (reports.isEmpty()) {
            throw new RuntimeException("No reports found for this developer under this lead");
        }

        return reports;
    }

    public Report save(ReportDTO dto, String email) {

        Report r = new Report();

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        r.setUser(user);

        // ✅ SET STATUS
        r.setStatus(dto.getStatus());

        // ✅ SET MONTH
        RefMonth rm = refMonthRepo.findById(dto.getRefMonthId())
                .orElseThrow(() -> new RuntimeException("Month not found"));
        r.setRefMonth(rm);
        r.setRefMonthId(rm.getId());  // 🔥 THIS WAS MISSING

        return reportRepo.save(r);
    }

    public Report getOrCreateCurrentReport(String email) {

        // ✅ 1. Get user
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ 2. Get current month & year
        LocalDate now = LocalDate.now();
        String month = now.getMonth().toString().substring(0, 3); // JAN, FEB...
        int year = now.getYear();

        // Format → Jan, Feb...
        month = month.substring(0, 1).toUpperCase() + month.substring(1).toLowerCase();

        // ✅ 3. Find RefMonth
        RefMonth refMonth = refMonthRepo.findByMonthAndYear(month, year)
                .orElseThrow(() -> new RuntimeException("Month not found"));

        // ✅ 4. Check if report exists
        Optional<Report> existing =
                reportRepo.findByUserIdAndRefMonthId(user.getId(), refMonth.getId());

        if (existing.isPresent()) {
            return existing.get(); // ✅ return existing
        }

        // ✅ 5. Create new report
        Report newReport = new Report();
        newReport.setUser(user);
        newReport.setRefMonth(refMonth);

        // 🔥 IMPORTANT (this was your missing part earlier)
        newReport.setRefMonthId(refMonth.getId());

        newReport.setStatus("PENDING");

        return reportRepo.save(newReport);
    }


    public List<Report> getAllReports() {
        return reportRepo.findAll();
    }

    public List<Report> getReportsForLead(Long leadId, Long monthId) {

        if (leadId == null || monthId == null) {
            throw new RuntimeException("LeadId and MonthId are required");
        }

//        return reportRepo.findByUser_Lead_IdAndRefMonthId(leadId, monthId);
        return null;
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

//        if (!report.getUser().getLead().getId().equals(leadId)) {
//            throw new RuntimeException("Access denied");
//        }

        return report;
    }

    public List<Report> getReportsByDeveloperAndMonth(
            Long developerId,
            Long leadId,
            Long monthId) {

//        return reportRepo.findByUserIdAndUser_Lead_IdAndRefMonthId(
//                developerId,
//                leadId,
//                monthId
//        );
        return null;
    }

    public List<Report> getAllReportsByLead(Long leadId) {
//        return reportRepo.findByUser_Lead_Id(leadId);
        return null;
    }


}