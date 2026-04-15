package com.ionidea.mothramxbe.tasks.service;

import com.ionidea.mothramxbe.tasks.dto.JiraEntryDTO;
import com.ionidea.mothramxbe.tasks.model.JiraEntry;
import com.ionidea.mothramxbe.tasks.model.Report;
import com.ionidea.mothramxbe.tasks.repository.JiraEntryRepository;
import com.ionidea.mothramxbe.tasks.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JiraEntryService {

    @Autowired
    private JiraEntryRepository jiraRepo;

    @Autowired
    private ReportRepository reportRepo;

    public JiraEntry save(JiraEntryDTO dto) {

        JiraEntry j = new JiraEntry();

        j.setTicketId(dto.getTicketId());
        j.setDescription(dto.getDescription());
        j.setStoryPoints(dto.getStoryPoints());
        j.setDaysSpent(dto.getDaysSpent());
        j.setRemaining(dto.getRemaining());

        Report r = reportRepo.findById(dto.getReportId()).orElse(null);
        j.setReport(r);

        return jiraRepo.save(j);
    }

    public List<JiraEntry> getAll() {
        return jiraRepo.findAll();
    }

    public void delete(Long id) {
        jiraRepo.deleteById(id);
    }

}