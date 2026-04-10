package com.ionidea.mothramxbe.tasks.controller;

import com.ionidea.mothramxbe.tasks.dto.JiraEntryDTO;
import com.ionidea.mothramxbe.tasks.model.JiraEntry;
import com.ionidea.mothramxbe.tasks.service.JiraEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jira")
@CrossOrigin("*")
public class JiraEntryController {

    @Autowired
    private JiraEntryService jiraService;

    @PreAuthorize("hasRole('DEVELOPER')")
    @PostMapping
    public JiraEntry save(@RequestBody JiraEntryDTO dto) {
        return jiraService.save(dto);
    }

    @PreAuthorize("hasRole('DEVELOPER')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        jiraService.delete(id);
    }

}