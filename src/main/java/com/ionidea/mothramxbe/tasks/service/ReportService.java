package com.ionidea.mothramxbe.tasks.service;

import com.ionidea.mothramxbe.security.model.User;
import com.ionidea.mothramxbe.security.repository.UserRepository;
import com.ionidea.mothramxbe.system.entity.Holiday;
import com.ionidea.mothramxbe.system.entity.RefMonth;
import com.ionidea.mothramxbe.system.repository.HolidayRepository;
import com.ionidea.mothramxbe.system.repository.RefMonthRepository;
import com.ionidea.mothramxbe.tasks.dto.ReportResponseDTO;
import com.ionidea.mothramxbe.tasks.model.LeaveEntry;
import com.ionidea.mothramxbe.tasks.model.Report;
import com.ionidea.mothramxbe.tasks.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private RefMonthRepository refMonthRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ReportRepository reportRepo;

    @Autowired
    private HolidayRepository holidayRepo;

    // =========================
    // CREATE OR GET CURRENT REPORT
    // =========================
    public Report getOrCreateCurrentReport(String email) {

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDate now = LocalDate.now();
        String month = now.getMonth().toString().substring(0, 3);
        int year = now.getYear();

        month = month.substring(0, 1).toUpperCase() + month.substring(1).toLowerCase();

        RefMonth refMonth = refMonthRepo.findByMonthAndYear(month, year)
                .orElseThrow(() -> new RuntimeException("Month not found"));

        Optional<Report> existing =
                reportRepo.findByUserIdAndRefMonthId(user.getId(), refMonth.getId());

        // 🔥 HANDLE EXISTING REPORT (VERY IMPORTANT FIX)
        if (existing.isPresent()) {

            Report report = existing.get();

            // ✅ FIX NULL VALUES (prevents NullPointerException)
            if (report.getIsLeaveSubmittedFirst() == null) {
                report.setIsLeaveSubmittedFirst(false);
            }

            if (report.getTotalWorkingDays() == null) {
                int totalWorkingDays = calculateTotalWorkingDays(refMonth);
                report.setTotalWorkingDays(totalWorkingDays);
            }

            if (report.getEffectiveWorkingDays() == null) {
                report.setEffectiveWorkingDays(0);
            }

            if (report.getLoggedWorkingDays() == null) {
                report.setLoggedWorkingDays(0);
            }

            // ✅ SAVE only if something was null (safe to always save too)
            return reportRepo.save(report);
        }

        // 🔥 CREATE NEW REPORT
        Report report = new Report();
        report.setUser(user);
        report.setRefMonth(refMonth);
        report.setLeadStatus("PENDING");

        // ✅ TOTAL WORKING DAYS
        int totalWorkingDays = calculateTotalWorkingDays(refMonth);
        report.setTotalWorkingDays(totalWorkingDays);

        // ✅ DEFAULT VALUES (IMPORTANT)
        report.setEffectiveWorkingDays(0);
        report.setLoggedWorkingDays(0);
        report.setIsLeaveSubmittedFirst(false);

        return reportRepo.save(report);
    }

    // =========================
    // LOCK LEAVES
    // =========================
    public void lockLeaves(String email) {

        Report report = getOrCreateCurrentReport(email);

        if (report.getLeaveEntries() == null || report.getLeaveEntries().isEmpty()) {
            throw new RuntimeException("Add leaves before locking");
        }

        int totalWorkingDays = report.getTotalWorkingDays();

        // 🔥 CALCULATE LEAVE DAYS FROM duration
        int leaveDays = report.getLeaveEntries().stream()
                .mapToInt(this::convertLeaveToDays)
                .sum();

        int effectiveWorkingDays = totalWorkingDays - leaveDays;

        report.setEffectiveWorkingDays(effectiveWorkingDays);

        // ⚠️ Used as lock flag
        report.setIsLeaveSubmittedFirst(true);

        reportRepo.save(report);
    }

    // =========================
    // CONVERT LEAVE TO DAYS
    // =========================
    private int convertLeaveToDays(LeaveEntry leave) {

        if (leave.getDuration() == null) return 0;

        switch (leave.getDuration().toUpperCase()) {
            case "FULL_DAY":
                return 1;

            case "HALF_DAY":
                return 1; // ⚠️ simplified (you can change later)

            default:
                return 0;
        }
    }

    // =========================
    // CALCULATE WORKING DAYS
    // =========================
    private int calculateTotalWorkingDays(RefMonth refMonth) {

        int year = refMonth.getYear();
        int monthValue = getMonthNumber(refMonth.getMonth());

        LocalDate start = LocalDate.of(year, monthValue, 1);
        int daysInMonth = start.lengthOfMonth();

        int workingDays = 0;

        List<Holiday> holidays = null;
//                holidayRepo.findByYearAndMonth_Id(year, refMonth.getId());

        List<Long> holidayDays = holidays.stream()
                .map(Holiday::getId)
                .toList();

        for (int i = 1; i <= daysInMonth; i++) {

            LocalDate date = LocalDate.of(year, monthValue, i);

            if (date.getDayOfWeek() == DayOfWeek.SATURDAY ||
                    date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                continue;
            }

            if (holidayDays.contains(i)) {
                continue;
            }

            workingDays++;
        }

        return workingDays;
    }

    private int getMonthNumber(String month) {

        switch (month.toLowerCase()) {
            case "jan":
                return 1;
            case "feb":
                return 2;
            case "mar":
                return 3;
            case "apr":
                return 4;
            case "may":
                return 5;
            case "jun":
                return 6;
            case "jul":
                return 7;
            case "aug":
                return 8;
            case "sep":
                return 9;
            case "oct":
                return 10;
            case "nov":
                return 11;
            case "dec":
                return 12;
            default:
                throw new RuntimeException("Invalid month: " + month);
        }
    }

    // =========================
    // GET REPORT
    // =========================
    public ReportResponseDTO getCurrentReportWithSummary(String email) {
        Report report = getOrCreateCurrentReport(email);
        return mapToDTO(report);
    }

    // =========================
    // SUBMIT
    // =========================
    public ReportResponseDTO submitReport(String email) {

        Report report = getOrCreateCurrentReport(email);

        if (!report.getLeadStatus().equals("PENDING")) {
            throw new RuntimeException("Only PENDING report can be submitted");
        }

        report.setLeadStatus("SUBMITTED");
        reportRepo.save(report);

        return mapToDTO(report);
    }

    // =========================
    // DTO MAPPING
    // =========================
    private ReportResponseDTO mapToDTO(Report report) {

        ReportResponseDTO dto = new ReportResponseDTO();

        dto.setId(report.getId());
        dto.setUserId(report.getUser().getId());
        dto.setUserName(report.getUser().getName());

        dto.setRefMonthId(report.getRefMonth().getId());
        dto.setMonth(report.getRefMonth().getMonth());
        dto.setYear(report.getRefMonth().getYear());

        dto.setStatus(report.getLeadStatus());
        dto.setLeaveSubmittedFirst(
                Boolean.TRUE.equals(report.getIsLeaveSubmittedFirst())
        );

        dto.setTotalWorkingDays(report.getTotalWorkingDays());
        dto.setEffectiveWorkingDays(report.getEffectiveWorkingDays());
        dto.setLoggedWorkingDays(report.getLoggedWorkingDays());

        dto.setJiraEntries(report.getJiraEntries());
        dto.setLeaveEntries(report.getLeaveEntries());

        return dto;
    }

}