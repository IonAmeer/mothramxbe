package com.ionidea.mothramxbe.teamtasks.service;

import com.ionidea.mothramxbe.teamtasks.dto.TeamTaskDTO;

import java.util.List;

public interface TeamTaskService {

    List<TeamTaskDTO> getReportsForLead(Long leadId, Long monthId);

    TeamTaskDTO updateStatus(Long reportId, String status, String email, String reason);

}
