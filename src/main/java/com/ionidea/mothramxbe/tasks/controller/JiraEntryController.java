package com.ionidea.mothramxbe.tasks.controller;

import com.ionidea.mothramxbe.tasks.dto.JiraEntryDTO;
import com.ionidea.mothramxbe.tasks.model.JiraEntry;
import com.ionidea.mothramxbe.tasks.service.JiraEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jira")
public class JiraEntryController {

    @Autowired
    private JiraEntryService jiraService;

    @PreAuthorize("hasAuthority('TASK_CREATE')")
    @PostMapping
    public JiraEntry save(@RequestBody JiraEntryDTO dto) {
        return jiraService.save(dto);
    }

    @PreAuthorize("hasAuthority('TASK_DELETE')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        jiraService.delete(id);
    }

}