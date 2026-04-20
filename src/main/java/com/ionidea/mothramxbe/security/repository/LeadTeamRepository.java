package com.ionidea.mothramxbe.security.repository;

import com.ionidea.mothramxbe.security.model.LeadTeam;
import com.ionidea.mothramxbe.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LeadTeamRepository extends JpaRepository<LeadTeam, Long> {

    // ✅ Check if developer already assigned to a lead
    boolean existsByDeveloper(User developer);

    // ✅ Check if specific lead-developer pair exists
    boolean existsByLeadIdAndDeveloperId(Long leadId, Long developerId);

    // ✅ Get mapping by developer
    Optional<LeadTeam> findByDeveloper(User developer);

    // ✅ Get all developers under a lead
    List<LeadTeam> findByLeadId(Long leadId);

    // ✅ Delete mapping by developer
    void deleteByDeveloper(User developer);
}