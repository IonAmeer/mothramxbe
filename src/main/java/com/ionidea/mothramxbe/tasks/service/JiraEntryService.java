package com.ionidea.mothramxbe.tasks.service;

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

    public JiraEntry save(JiraEntry je) {

        if (je.getReport() == null || je.getReport().getId() == null) {
            throw new RuntimeException("Report ID is required");
        }

        Report rep = reportRepo
                .findById(je.getReport().getId())
                .orElseThrow(() -> new RuntimeException("Report not found"));

        je.setReport(rep);

        return jiraRepo.save(je);
    }

    public List<JiraEntry> getAll() {
        return jiraRepo.findAll();
    }

    public void delete(Integer id) {
        jiraRepo.deleteById(id);
    }

}