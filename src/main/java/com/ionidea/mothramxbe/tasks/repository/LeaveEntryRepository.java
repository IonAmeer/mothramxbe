package com.ionidea.mothramxbe.tasks.repository;

import com.ionidea.mothramxbe.tasks.model.LeaveEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaveEntryRepository extends JpaRepository<LeaveEntry, Long> {

    List<LeaveEntry> findByReportId(Long reportId);

}