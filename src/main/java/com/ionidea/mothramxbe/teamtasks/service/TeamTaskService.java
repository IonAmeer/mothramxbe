package com.ionidea.mothramxbe.teamtasks.service;

import com.ionidea.mothramxbe.teamtasks.dto.TeamTaskDTO;

import java.util.List;

public interface TeamTaskService {

    List<TeamTaskDTO> getReportsForLead(Integer leadId, Integer monthId);

    TeamTaskDTO updateStatus(Integer reportId, String status, String email, String reason);

}
