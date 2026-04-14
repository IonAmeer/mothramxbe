package com.ionidea.mothramxbe.tasks.repository;

import com.ionidea.mothramxbe.tasks.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByUser_Lead_IdAndRefMonthId(Long leadId, Long monthId);

    List<Report> findByUserIdAndUser_Lead_Id(Long userId, Long leadId);

    List<Report> findByUserIdAndUser_Lead_IdAndRefMonthId(Long userId, Long leadId, Long refMonthId);

    List<Report> findByUser_Lead_Id(Long leadId);

}