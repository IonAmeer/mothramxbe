package com.ionidea.mothramxbe.teamtasks.service;

import com.ionidea.mothramxbe.tasks.model.Report;
import com.ionidea.mothramxbe.tasks.service.ReportService;
import com.ionidea.mothramxbe.teamtasks.dto.TeamTaskDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamTaskServiceImpl implements TeamTaskService {

    @Autowired
    private ReportService reportService;

    @Override
    public List<TeamTaskDTO> getReportsForLead(Integer leadId, Integer monthId) {
        List<Report> reports = reportService.getReportsForLead(leadId, monthId);
        return reports.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public TeamTaskDTO updateStatus(Integer reportId, String status, String email, String reason) {
        Report report = reportService.updateStatus(reportId, status, email, "LEAD", reason);
        return convertToDTO(report);
    }

    // 🔁 Convert Entity → DTO
    private TeamTaskDTO convertToDTO(Report report) {
        return new TeamTaskDTO(
                report.getId(),
                report.getUser().getName(),
                report.getStatus(),
                report.getRefMonthId(),
                report.getApprovedBy()
        );
    }

}