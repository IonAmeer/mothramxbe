package com.ionidea.mothramxbe.tasks.service;

import com.ionidea.mothramxbe.security.model.User;
import com.ionidea.mothramxbe.security.repository.UserRepository;
import com.ionidea.mothramxbe.tasks.dto.LeaveEntryDTO;
import com.ionidea.mothramxbe.tasks.model.LeaveEntry;
import com.ionidea.mothramxbe.tasks.model.LeaveType;
import com.ionidea.mothramxbe.tasks.model.Report;
import com.ionidea.mothramxbe.tasks.repository.LeaveEntryRepository;
import com.ionidea.mothramxbe.tasks.repository.LeaveTypeRepository;
import com.ionidea.mothramxbe.tasks.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveEntryService {

    @Autowired
    private LeaveEntryRepository repo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private LeaveTypeRepository leaveTypeRepo;

    @Autowired
    private ReportRepository reportRepo;

    public LeaveEntry save(LeaveEntryDTO dto) {

        LeaveEntry le = new LeaveEntry();

        le.setDate(dto.getDate());
        le.setDuration(dto.getDuration());
        le.setReason(dto.getReason());

        // ✅ LeaveType
        LeaveType lt = leaveTypeRepo.findById(dto.getLeaveTypeId())
                .orElseThrow(() -> new RuntimeException("Leave type not found"));
        le.setLeaveType(lt);

        // ✅ Report
        Report r = reportRepo.findById(dto.getReportId())
                .orElseThrow(() -> new RuntimeException("Report not found"));
        le.setReport(r);

        return repo.save(le);
    }

    public List<LeaveEntry> getAll() {
        return repo.findAll();
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public List<LeaveEntry> getByReportId(Long reportId) {
        return repo.findByReportId(reportId);
    }

    public LeaveEntry update(Long id, LeaveEntryDTO dto) {

        // 🔥 1. Get existing leave
        LeaveEntry existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        // 🔥 2. Update fields
        existing.setDate(dto.getDate());
        existing.setDuration(dto.getDuration());
        existing.setReason(dto.getReason());

        // 🔥 3. Update Leave Type
        if (dto.getLeaveTypeId() != null) {
            LeaveType lt = leaveTypeRepo.findById(dto.getLeaveTypeId())
                    .orElseThrow(() -> new RuntimeException("Leave Type not found"));
            existing.setLeaveType(lt);
        }

        // 🔥 4. Update Report (optional but safe)
        if (dto.getReportId() != null) {
            Report r = reportRepo.findById(dto.getReportId())
                    .orElseThrow(() -> new RuntimeException("Report not found"));
            existing.setReport(r);
        }

        // 🔥 5. Save
        return repo.save(existing);
    }

}