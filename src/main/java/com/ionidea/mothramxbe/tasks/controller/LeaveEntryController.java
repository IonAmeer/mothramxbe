package com.ionidea.mothramxbe.tasks.controller;

import com.ionidea.mothramxbe.tasks.dto.LeaveEntryDTO;
import com.ionidea.mothramxbe.tasks.model.LeaveEntry;
import com.ionidea.mothramxbe.tasks.service.LeaveEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/leave-entries")
public class LeaveEntryController {

    @Autowired
    private LeaveEntryService service;

    @PostMapping
    @PreAuthorize("hasRole('DEVELOPER')")
    public LeaveEntry save(@RequestBody LeaveEntryDTO dto) {
        return service.save(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

}
