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

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ReportService {

    @Autowired
    private RefMonthRepository refMonthRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ReportRepository reportRepo;


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
        Optional<Report> existing = null;
//                reportRepo.findByUserIdAndRefMonthId(user.getId(), refMonth.getId());

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

}