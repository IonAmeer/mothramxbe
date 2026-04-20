package com.ionidea.mothramxbe.tasks.service;

import com.ionidea.mothramxbe.security.model.User;
import com.ionidea.mothramxbe.security.repository.UserRepository;
import com.ionidea.mothramxbe.tasks.dto.JiraEntryDTO;
import com.ionidea.mothramxbe.tasks.model.JiraEntry;
import com.ionidea.mothramxbe.tasks.model.Report;
import com.ionidea.mothramxbe.tasks.repository.JiraEntryRepository;
import com.ionidea.mothramxbe.tasks.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class JiraEntryService {

    @Autowired
    private JiraEntryRepository jiraRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ReportRepository reportRepo;

    public JiraEntry save(JiraEntryDTO dto) {

        JiraEntry j = new JiraEntry();

        j.setTicketId(dto.getTicketId());
        j.setDescription(dto.getDescription());
        j.setStoryPoints(dto.getStoryPoints());
        j.setDaysSpent(dto.getDaysSpent());
        j.setRemaining(dto.getRemaining());

        // ✅ Get Report
        Report r = reportRepo.findById(dto.getReportId())
                .orElseThrow(() -> new RuntimeException("Report not found"));
        j.setReport(r);

        return jiraRepo.save(j);
    }

    public List<JiraEntry> getAll() {
        return jiraRepo.findAll();
    }

    public void delete(Long id) {
        jiraRepo.deleteById(id);
    }

    public List<JiraEntry> getByReportId(Long reportId) {
        return jiraRepo.findByReportId(reportId);
    }


    public JiraEntry update(Long id, JiraEntryDTO dto) {

        // 🔥 1. Get existing record
        JiraEntry existing = jiraRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Jira Entry not found"));

        // 🔥 2. Update fields
        existing.setTicketId(dto.getTicketId());
        existing.setDescription(dto.getDescription());
        existing.setStoryPoints(dto.getStoryPoints());
        existing.setDaysSpent(dto.getDaysSpent());
        existing.setRemaining(dto.getRemaining());

        // 🔥 3. (Optional but safe) update report if needed
        if (dto.getReportId() != null) {
            Report r = reportRepo.findById(dto.getReportId())
                    .orElseThrow(() -> new RuntimeException("Report not found"));
            existing.setReport(r);
        }

        // 🔥 4. Save updated object
        return jiraRepo.save(existing);
    }
}