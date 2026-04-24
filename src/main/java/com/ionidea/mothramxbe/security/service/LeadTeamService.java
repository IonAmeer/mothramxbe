package com.ionidea.mothramxbe.security.service;

import com.ionidea.mothramxbe.exception.DuplicateResourceException;
import com.ionidea.mothramxbe.exception.ResourceNotFoundException;
import com.ionidea.mothramxbe.security.dto.LeadTeamResponseDTO;
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
    public String assignDeveloperToLead(Long leadId, Long developerId) {

        if (leadId.equals(developerId)) {
            throw new RuntimeException("Lead and Developer cannot be same");
        }

        if (repo.existsByLeadIdAndDeveloperId(leadId, developerId)) {
            throw new DuplicateResourceException("Mapping already exists");
        }

        User lead = userRepository.findById(leadId)
                .orElseThrow(() -> new ResourceNotFoundException("Lead not found"));

        User developer = userRepository.findById(developerId)
                .orElseThrow(() -> new ResourceNotFoundException("Developer not found"));

        // ❗ IMPORTANT RULE: developer cannot already be assigned
        if (repo.existsByDeveloper(developer)) {
            throw new RuntimeException("Developer already assigned to another Lead");
        }

        LeadTeam lt = new LeadTeam();
        lt.setLead(lead);
        lt.setDeveloper(developer);

        repo.save(lt);

        return "Developer assigned to Lead successfully";
    }

    public LeadTeamResponseDTO getDevelopersByLead(Long leadId) {

        List<LeadTeam> mappings = repo.findByLeadId(leadId);

        if (mappings.isEmpty()) {
            throw new ResourceNotFoundException("No developers found for this lead or no lead exists for id: " + leadId);
        }

        // ✅ Get lead info (same for all)
        User lead = mappings.get(0).getLead();

        // ✅ Map developers list
        List<LeadTeamResponseDTO.DeveloperDTO> developers =
                mappings.stream()
                        .map(m -> {
                            User dev = m.getDeveloper();
                            return new LeadTeamResponseDTO.DeveloperDTO(
                                    dev.getId(),
                                    dev.getName(),
                                    dev.getEmail()
                            );
                        })
                        .toList();

        return new LeadTeamResponseDTO(
                lead.getId(),
                lead.getName(),
                developers
        );
    }

    public String removeMapping(Long id) {

        LeadTeam lt = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mapping not found"));

        repo.delete(lt);

        return "Mapping deleted successfully";
    }

    public List<LeadTeamResponseDTO> getAllLeadsWithDevelopers() {

        List<User> leads = userRepository.findAll().stream()
                .filter(user -> user.getUserRoles().stream()
                        .anyMatch(ur -> ur.getRole().getName().equals("LEAD")))
                .toList();

        return leads.stream().map(lead -> {

            List<LeadTeam> mappings = repo.findByLeadId(lead.getId());

            List<LeadTeamResponseDTO.DeveloperDTO> developers =
                    mappings.stream()
                            .map(m -> new LeadTeamResponseDTO.DeveloperDTO(
                                    m.getDeveloper().getId(),
                                    m.getDeveloper().getName(),
                                    m.getDeveloper().getEmail()
                            ))
                            .toList();

            return new LeadTeamResponseDTO(
                    lead.getId(),
                    lead.getName(),
                    developers
            );

        }).toList();
    }

    @Transactional
    public void removeMapping(Long leadId, Long developerId) {

        LeadTeam mapping = repo.findByLeadId(leadId).stream()
                .filter(m -> m.getDeveloper().getId().equals(developerId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Mapping not found"));

        repo.delete(mapping);
    }

}