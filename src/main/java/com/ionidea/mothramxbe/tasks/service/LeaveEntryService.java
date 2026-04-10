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

@Service
public class LeaveEntryService {

    @Autowired
    private LeaveEntryRepository repo;

    @Autowired
    private LeaveTypeRepository leaveTypeRepo;

    @Autowired
    private ReportRepository reportRepo;

    public LeaveEntry save(LeaveEntryDTO dto) {

        LeaveEntry le = new LeaveEntry();

        le.setDate(dto.getDate());
        le.setDuration(dto.getDuration());
        le.setReason(dto.getReason());

        LeaveType lt = leaveTypeRepo.findById(dto.getLeaveTypeId()).orElse(null);
        le.setLeaveType(lt);

        Report r = reportRepo.findById(dto.getReportId()).orElse(null);
        le.setReport(r);

        return repo.save(le);
    }

    public List<LeaveEntry> getAll() {
        return repo.findAll();
    }

    public void delete(Integer id) {
        repo.deleteById(id);
    }

}