package com.ionidea.mothramxbe.tasks.controller;

import com.ionidea.mothramxbe.tasks.dto.LeaveEntryDTO;
import com.ionidea.mothramxbe.tasks.model.LeaveEntry;
import com.ionidea.mothramxbe.tasks.repository.LeaveEntryRepository;
import com.ionidea.mothramxbe.tasks.repository.LeaveTypeRepository;
import com.ionidea.mothramxbe.tasks.repository.ReportRepository;
import com.ionidea.mothramxbe.tasks.service.LeaveEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leave-entries")
@CrossOrigin("*")
public class LeaveEntryController {

    @Autowired
    private LeaveEntryService service;

    @PreAuthorize("hasRole('DEVELOPER')")
    @PostMapping
    public LeaveEntry save(@RequestBody LeaveEntryDTO dto) {
        return service.save(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

}
