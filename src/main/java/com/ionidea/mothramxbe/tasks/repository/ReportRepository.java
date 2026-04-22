package com.ionidea.mothramxbe.tasks.repository;

import com.ionidea.mothramxbe.tasks.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    @NativeQuery(value = "select r.* from reports r join lead_team lt on r.user_id = lt.developer_id where lt.lead_id = :leadId and r.ref_month_id = :monthId")
    List<Report> findReportByLead(Long leadId, Long monthId);

}