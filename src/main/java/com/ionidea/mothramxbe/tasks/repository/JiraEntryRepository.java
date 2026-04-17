package com.ionidea.mothramxbe.tasks.repository;

import com.ionidea.mothramxbe.tasks.model.JiraEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JiraEntryRepository extends JpaRepository<JiraEntry, Long> {

    List<JiraEntry> findByReportId(Long reportId);

}