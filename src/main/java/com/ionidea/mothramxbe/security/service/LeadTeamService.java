package com.ionidea.mothramxbe.security.service;

import com.ionidea.mothramxbe.exception.ResourceNotFoundException;
import com.ionidea.mothramxbe.security.model.LeadTeam;
import com.ionidea.mothramxbe.security.model.User;
import com.ionidea.mothramxbe.security.repository.LeadTeamRepository;
import com.ionidea.mothramxbe.security.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeadTeamService {

    private final LeadTeamRepository repo;
    private final UserRepository userRepository;

    @Transactional
    public void assignDeveloperToLead(Long leadId, Long developerId) {

        if (repo.existsByLeadIdAndDeveloperId(leadId, developerId)) {
            throw new RuntimeException("Mapping already exists");
        }

        // a lead should not be assigned to a team

        User lead = userRepository.findById(leadId).orElseThrow(() -> new ResourceNotFoundException("Lead not found"));

        User developer = userRepository.findById(developerId).orElseThrow(() -> new ResourceNotFoundException("Developer not found"));

        LeadTeam ld = new LeadTeam();
        ld.setLead(lead);
        ld.setDeveloper(developer);

        repo.save(ld);
    }

    public List<LeadTeam> getDevelopersByLead(Long leadId) {
        return repo.findByLeadId(leadId);
    }

    public void removeMapping(Long id) {
        repo.deleteById(id);
    }


}
