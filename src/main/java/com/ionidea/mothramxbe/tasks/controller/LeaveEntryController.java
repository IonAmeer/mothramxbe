package com.ionidea.mothramxbe.tasks.controller;

import com.ionidea.mothramxbe.tasks.model.LeaveEntry;
import com.ionidea.mothramxbe.tasks.service.LeaveEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leave-entries")
@CrossOrigin("*")
public class LeaveEntryController {

    @Autowired
    private LeaveEntryService service;

    @PreAuthorize("hasAuthority('DEVELOPER')")
    @PostMapping
    public LeaveEntry save(@RequestBody LeaveEntry le) {
        return service.save(le);
    }

    @PreAuthorize("hasAuthority('LEAD')")
    @GetMapping
    public List<LeaveEntry> getAll() {
        return service.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }

}
