package com.ionidea.mothramxbe.security.repository;

import com.ionidea.mothramxbe.security.model.LeadTeam;
import com.ionidea.mothramxbe.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LeadTeamRepository extends JpaRepository<LeadTeam, Long> {

    boolean existsByDeveloper(User developer);

    boolean existsByLeadIdAndDeveloperId(Long leadId, Long developerId);

    Optional<LeadTeam> findByDeveloper(User developer);

    List<LeadTeam> findByLeadId(Long leadId);

    void deleteByDeveloper(User developer);

    List<LeadTeam> findAll();

    Optional<LeadTeam> findByLeadIdAndDeveloperId(Long leadId, Long developerId);

}