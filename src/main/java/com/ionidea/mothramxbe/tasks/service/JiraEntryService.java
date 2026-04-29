package com.ionidea.mothramxbe.tasks.service;

import com.ionidea.mothramxbe.exception.BadRequestException;
import com.ionidea.mothramxbe.tasks.dto.JiraEntryDTO;
import com.ionidea.mothramxbe.tasks.model.JiraEntry;
import com.ionidea.mothramxbe.tasks.model.Report;
import com.ionidea.mothramxbe.tasks.repository.JiraEntryRepository;
import com.ionidea.mothramxbe.tasks.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JiraEntryService {

    @Autowired
    private JiraEntryRepository jiraRepo;

    @Autowired
    private ReportRepository reportRepo;

    // =========================
    // 🔥 MAPPER
    // =========================
    public static JiraEntryDTO mapToDTO(JiraEntry j) {

        JiraEntryDTO dto = new JiraEntryDTO();

        dto.setId(j.getId());
        dto.setTicketId(j.getTicketId());
        dto.setDescription(j.getDescription());
        dto.setStoryPoints(j.getStoryPoints());
        dto.setDaysSpent(j.getDaysSpent());
        dto.setRemaining(j.getRemaining());

        return dto;
    }

    // =========================
    // CREATE
    // =========================
    @Transactional
    public JiraEntryDTO save(JiraEntryDTO dto) {

        Report report = reportRepo.findById(dto.getReportId())
                .orElseThrow(() -> new RuntimeException("Report not found"));

        // ❌ RULE: Leaves must be locked
        if (!Boolean.TRUE.equals(report.getIsLeaveSubmittedFirst())) {
            throw new BadRequestException("Please finalize leaves before adding Jira entries");
        }

        double updatedLoggedDays = calculateAndValidate(report, dto.getDaysSpent(), null);

        // ✅ Update report
        report.setLoggedWorkingDays(updatedLoggedDays);
        reportRepo.save(report);

        // ✅ Save Jira
        JiraEntry jira = new JiraEntry();
        jira.setTicketId(dto.getTicketId());
        jira.setDescription(dto.getDescription());
        jira.setStoryPoints(dto.getStoryPoints());
        jira.setDaysSpent(dto.getDaysSpent());
        jira.setRemaining(dto.getRemaining());
        jira.setReport(report);

        JiraEntry saved = jiraRepo.save(jira);

        return mapToDTO(saved);
    }

    // =========================
    // UPDATE
    // =========================
    @Transactional
    public JiraEntryDTO update(Long id, JiraEntryDTO dto) {

        JiraEntry existing = jiraRepo.findById(id)
                .orElseThrow(() -> new BadRequestException("Jira Entry not found"));

        Report report = existing.getReport();

        if (!Boolean.TRUE.equals(report.getIsLeaveSubmittedFirst())) {
            throw new BadRequestException("Please finalize leaves before updating Jira entries");
        }

        double updatedLoggedDays = calculateAndValidate(report, dto.getDaysSpent(), id);

        report.setLoggedWorkingDays(updatedLoggedDays);
        reportRepo.save(report);

        existing.setTicketId(dto.getTicketId());
        existing.setDescription(dto.getDescription());
        existing.setStoryPoints(dto.getStoryPoints());
        existing.setDaysSpent(dto.getDaysSpent());
        existing.setRemaining(dto.getRemaining());

        JiraEntry updated = jiraRepo.save(existing);

        return mapToDTO(updated);
    }

    // =========================
    // DELETE
    // =========================
    @Transactional
    public void delete(Long id) {

        JiraEntry existing = jiraRepo.findById(id)
                .orElseThrow(() -> new BadRequestException("Jira Entry not found"));

        Report report = existing.getReport();

        double currentLogged = report.getLoggedWorkingDays() != null ? report.getLoggedWorkingDays() : 0;
        int daysToRemove = existing.getDaysSpent() != null ? existing.getDaysSpent() : 0;

        report.setLoggedWorkingDays(currentLogged - daysToRemove);
        reportRepo.save(report);

        jiraRepo.deleteById(id);
    }

    // =========================
    // GET
    // =========================
    public List<JiraEntryDTO> getByReportId(Long reportId) {
        return jiraRepo.findByReportId(reportId)
                .stream()
                .map(JiraEntryService::mapToDTO)
                .collect(Collectors.toList());
    }

    // =========================
    // 🔥 CORE VALIDATION
    // =========================
    private int calculateAndValidate(Report report, Integer newDays, Long excludeId) {

        List<JiraEntry> jiraEntries = jiraRepo.findByReportId(report.getId());

        int total = 0;

        for (JiraEntry j : jiraEntries) {

            if (excludeId != null && j.getId().equals(excludeId)) {
                continue;
            }

            total += (j.getDaysSpent() != null) ? j.getDaysSpent() : 0;
        }

        int newDaysInt = newDays != null ? newDays : 0;
        int finalTotal = total + newDaysInt;

        double effectiveDays = report.getEffectiveWorkingDays() != null
                ? report.getEffectiveWorkingDays()
                : 0;

        if (finalTotal > effectiveDays) {
            throw new BadRequestException(
                    "Logged days (" + finalTotal + ") exceed allowed working days (" + effectiveDays + ")"
            );
        }

        return finalTotal;
    }

}