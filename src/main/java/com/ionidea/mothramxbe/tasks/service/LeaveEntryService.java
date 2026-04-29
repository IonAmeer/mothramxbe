package com.ionidea.mothramxbe.tasks.service;

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
import java.util.stream.Collectors;

@Service
public class LeaveEntryService {

    @Autowired
    private LeaveEntryRepository repo;

    @Autowired
    private LeaveTypeRepository leaveTypeRepo;

    @Autowired
    private ReportRepository reportRepo;

    // =========================
    // 🔥 MAPPER
    // =========================
    public static LeaveEntryDTO mapToDTO(LeaveEntry le) {

        LeaveEntryDTO dto = new LeaveEntryDTO();

        dto.setId(le.getId());
        dto.setDate(le.getDate());
        dto.setDuration(le.getDuration());
        dto.setReason(le.getReason());
        dto.setLeaveTypeId(le.getLeaveType().getId());

        if (le.getLeaveType() != null) {
            dto.setLeaveType(le.getLeaveType().getName());
        }

        return dto;
    }

    // =========================
    // CREATE
    // =========================
    public LeaveEntryDTO save(LeaveEntryDTO dto) {

        LeaveEntry le = new LeaveEntry();

        le.setDate(dto.getDate());
        le.setDuration(dto.getDuration());
        le.setReason(dto.getReason());

        LeaveType lt = leaveTypeRepo.findById(dto.getLeaveTypeId())
                .orElseThrow(() -> new RuntimeException("Leave type not found"));
        le.setLeaveType(lt);

        Report r = reportRepo.findById(dto.getReportId())
                .orElseThrow(() -> new RuntimeException("Report not found"));
        le.setReport(r);

        LeaveEntry saved = repo.save(le);

        return mapToDTO(saved);
    }

    // =========================
    // GET ALL
    // =========================
    public List<LeaveEntryDTO> getAll() {
        return repo.findAll()
                .stream()
                .map(LeaveEntryService::mapToDTO)
                .collect(Collectors.toList());
    }

    // =========================
    // GET BY REPORT
    // =========================
    public List<LeaveEntryDTO> getByReportId(Long reportId) {
        return repo.findByReportId(reportId)
                .stream()
                .map(LeaveEntryService::mapToDTO)
                .collect(Collectors.toList());
    }

    // =========================
    // DELETE
    // =========================
    public void delete(Long id) {
        repo.deleteById(id);
    }

    // =========================
    // UPDATE
    // =========================
    public LeaveEntryDTO update(Long id, LeaveEntryDTO dto) {

        LeaveEntry existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        existing.setDate(dto.getDate());
        existing.setDuration(dto.getDuration());
        existing.setReason(dto.getReason());

        if (dto.getLeaveTypeId() != null) {
            LeaveType lt = leaveTypeRepo.findById(dto.getLeaveTypeId())
                    .orElseThrow(() -> new RuntimeException("Leave Type not found"));
            existing.setLeaveType(lt);
        }

        if (dto.getReportId() != null) {
            Report r = reportRepo.findById(dto.getReportId())
                    .orElseThrow(() -> new RuntimeException("Report not found"));
            existing.setReport(r);
        }

        LeaveEntry updated = repo.save(existing);

        return mapToDTO(updated);
    }

}