package com.ionidea.mothramxbe.security.controller;

import com.ionidea.mothramxbe.security.model.LeadTeam;
import com.ionidea.mothramxbe.security.service.LeadTeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lead-team")
@RequiredArgsConstructor
public class LeadTeamController {

    private final LeadTeamService service;

    @PostMapping("/assign")
    public void assign(@RequestParam Long leadId, @RequestParam Long developerId) {
        service.assignDeveloperToLead(leadId, developerId);
    }

    @GetMapping("/lead/{leadId}")
    public List<LeadTeam> getByLead(@PathVariable Long leadId) {
        return service.getDevelopersByLead(leadId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.removeMapping(id);
    }


}
