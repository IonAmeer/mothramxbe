package com.ionidea.mothramxbe.tasks.controller;

import com.ionidea.mothramxbe.tasks.dto.LeaveEntryDTO;
import com.ionidea.mothramxbe.tasks.model.LeaveEntry;
import com.ionidea.mothramxbe.tasks.service.LeaveEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leave-entries")
public class LeaveEntryController {

    @Autowired
    private LeaveEntryService service;

    @PostMapping
    @PreAuthorize("hasAuthority('TASK_CREATE')")
    public LeaveEntry save(@RequestBody LeaveEntryDTO dto) {
        return service.save(dto);
    }

    @PreAuthorize("hasAuthority('TASK_DELETE')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PreAuthorize("hasAuthority('TASK_READ')")
    @GetMapping("/leave/report/{reportId}")
    public List<LeaveEntry> getByReport(@PathVariable Long reportId) {
        return service.getByReportId(reportId);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('TASK_UPDATE')")
    public LeaveEntry update(@PathVariable Long id, @RequestBody LeaveEntryDTO dto) {
        return service.update(id, dto);
    }

}
