package com.ionidea.mothramxbe.security.controller;

import com.ionidea.mothramxbe.security.dto.LeadTeamResponseDTO;
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

    // ✅ ASSIGN
    @PostMapping("/assign")
    public String assign(@RequestParam Long leadId,
                         @RequestParam Long developerId) {
        return service.assignDeveloperToLead(leadId, developerId);
    }

    // ✅ GET USING PARAM
    @GetMapping("/lead")
    public LeadTeamResponseDTO getByLead(@RequestParam Long leadId) {
        return service.getDevelopersByLead(leadId);
    }
    @GetMapping("/all")
    public List<LeadTeamResponseDTO> getAllLeadsWithDevelopers() {
        return service.getAllLeadsWithDevelopers();
    }

    @DeleteMapping("/remove")
    public String remove(@RequestParam Long leadId, @RequestParam Long developerId) {
        service.removeMapping(leadId, developerId);
        return "Developer removed successfully";
    }
    // ✅ DELETE USING PARAM
    @DeleteMapping("/delete")
    public String delete(@RequestParam Long id) {
        return service.removeMapping(id);
    }
}